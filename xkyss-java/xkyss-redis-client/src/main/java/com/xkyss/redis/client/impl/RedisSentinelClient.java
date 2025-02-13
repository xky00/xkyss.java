/*
 * Copyright 2019 Red Hat, Inc.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 * <p>
 * You may elect to redistribute this code under either of these licenses.
 */
package com.xkyss.redis.client.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.tracing.TracingPolicy;
import com.xkyss.redis.client.Command;
import com.xkyss.redis.client.PoolOptions;
import com.xkyss.redis.client.Redis;
import com.xkyss.redis.client.RedisConnectOptions;
import com.xkyss.redis.client.RedisConnection;
import com.xkyss.redis.client.RedisRole;
import com.xkyss.redis.client.RedisSentinelConnectOptions;
import com.xkyss.redis.client.Request;
import com.xkyss.redis.client.Response;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class RedisSentinelClient extends BaseRedisClient implements Redis {

  // we need some randomness, it doesn't need to be cryptographically secure
  private static final Random RANDOM = new Random();

  private static class Pair<L, R> {
    final L left;
    final R right;

    Pair(L left, R right) {
      this.left = left;
      this.right = right;
    }
  }

  private static final Logger LOG = LoggerFactory.getLogger(RedisSentinelClient.class);

  private final RedisSentinelConnectOptions connectOptions;
  private final AtomicReference<SentinelFailover> failover = new AtomicReference<>();

  public RedisSentinelClient(Vertx vertx, NetClientOptions tcpOptions, PoolOptions poolOptions, RedisSentinelConnectOptions connectOptions, TracingPolicy tracingPolicy) {
    super(vertx, tcpOptions, poolOptions, connectOptions, tracingPolicy);
    this.connectOptions = connectOptions;
    // validate options
    if (poolOptions.getMaxWaiting() < poolOptions.getMaxSize()) {
      throw new IllegalStateException("Invalid options: maxWaiting < maxSize");
    }
  }

  @Override
  public Future<RedisConnection> connect() {
    final Promise<RedisConnection> promise = vertx.promise();

    createConnectionInternal(connectOptions, connectOptions.getRole(), createConnection -> {
      if (createConnection.failed()) {
        promise.fail(createConnection.cause());
        return;
      }

      final PooledRedisConnection conn = createConnection.result();

      if (connectOptions.getRole() == RedisRole.SENTINEL || connectOptions.getRole() == RedisRole.REPLICA) {
        // it is possible that a replica is later promoted to a master, but that shouldn't be too big of a deal
        promise.complete(conn);
        return;
      }
      if (!connectOptions.isAutoFailover()) {
        // no auto failover, return the master connection directly
        promise.complete(conn);
        return;
      }

      SentinelFailover failover = setupFailover();
      RedisSentinelConnection sentinelConn = new RedisSentinelConnection(conn, failover);
      promise.complete(sentinelConn);
    });

    return promise.future();
  }

  private SentinelFailover setupFailover() {
    SentinelFailover result = this.failover.get();

    if (result == null) {
      result = new SentinelFailover(connectOptions.getMasterName(), this::createConnectionInternal);
      if (this.failover.compareAndSet(null, result)) {
        result.start();
      } else {
        result = this.failover.get();
      }
    }

    return result;
  }

  @Override
  public void close() {
    SentinelFailover failover = this.failover.get();
    if (failover != null) {
      failover.close();
    }
    super.close();
  }

  private Future<PooledRedisConnection> createConnectionInternal(RedisRole role) {
    Promise<PooledRedisConnection> promise = Promise.promise();
    createConnectionInternal(connectOptions, role, promise);
    return promise.future();
  }

  private void createConnectionInternal(RedisSentinelConnectOptions options, RedisRole role, Handler<AsyncResult<PooledRedisConnection>> onCreate) {

    final Handler<AsyncResult<RedisURI>> createAndConnect = resolve -> {
      if (resolve.failed()) {
        onCreate.handle(Future.failedFuture(resolve.cause()));
        return;
      }

      final RedisURI uri = resolve.result();
      final Request setup;

      // `SELECT` is only allowed on non-sentinel nodes
      // we don't send `READONLY` setup to replica nodes, because that's a cluster-only command
      if (role != RedisRole.SENTINEL && uri.select() != null) {
        setup = Request.cmd(Command.SELECT).arg(uri.select());
      } else {
        setup = null;
      }

      // wrap a new client
      connectionManager.getConnection(uri.baseUri(), setup).onComplete(onCreate);
    };

    switch (role) {
      case SENTINEL:
        resolveClient(this::isSentinelOk, options, createAndConnect);
        break;
      case MASTER:
        resolveClient(this::getMasterFromEndpoint, options, createAndConnect);
        break;
      case REPLICA:
        resolveClient(this::getReplicaFromEndpoint, options, createAndConnect);
        break;
    }
  }

  /**
   * We use the algorithm from http://redis.io/topics/sentinel-clients
   * to get a sentinel client and then do 'stuff' with it
   */
  private static void resolveClient(final Resolver checkEndpointFn, final RedisSentinelConnectOptions options, final Handler<AsyncResult<RedisURI>> callback) {
    // Because finding the master is going to be an async list we will terminate
    // when we find one then use promises...
    iterate(0, ConcurrentHashMap.newKeySet(), checkEndpointFn, options, iterate -> {
      if (iterate.failed()) {
        callback.handle(Future.failedFuture(iterate.cause()));
      } else {
        final Pair<Integer, RedisURI> found = iterate.result();
        // This is the endpoint that has responded so stick it on the top of
        // the list
        final List<String> endpoints = options.getEndpoints();
        String endpoint = endpoints.get(found.left);
        endpoints.set(found.left, endpoints.get(0));
        endpoints.set(0, endpoint);
        // now return the right address
        callback.handle(Future.succeededFuture(found.right));
      }
    });
  }

  private static void iterate(final int idx, final Set<Throwable> failures, final Resolver checkEndpointFn, final RedisSentinelConnectOptions argument, final Handler<AsyncResult<Pair<Integer, RedisURI>>> resultHandler) {
    // stop condition
    final List<String> endpoints = argument.getEndpoints();

    if (idx >= endpoints.size()) {
      StringBuilder message = new StringBuilder("Cannot connect to any of the provided endpoints");
      for (Throwable failure : failures) {
        message.append("\n- ").append(failure);
      }
      resultHandler.handle(Future.failedFuture(new RedisConnectException(message.toString())));
      return;
    }

    // attempt to perform operation
    checkEndpointFn.resolve(endpoints.get(idx), argument, res -> {
      if (res.succeeded()) {
        resultHandler.handle(Future.succeededFuture(new Pair<>(idx, res.result())));
      } else {
        // try again with next endpoint
        failures.add(res.cause());
        iterate(idx + 1, failures, checkEndpointFn, argument, resultHandler);
      }
    });
  }

  // begin endpoint check methods

  private void isSentinelOk(String endpoint, RedisConnectOptions argument, Handler<AsyncResult<RedisURI>> handler) {
    // we can't use the endpoint as is, it should not contain a database selection,
    // but can contain authentication
    final RedisURI uri = new RedisURI(endpoint);

    connectionManager.getConnection(uri.baseUri(), null)
      .onFailure(err -> handler.handle(Future.failedFuture(err)))
      .onSuccess(conn -> {
        // Send a command just to check we have a working node
        conn.send(Request.cmd(Command.PING), ping -> {
          if (ping.failed()) {
            handler.handle(Future.failedFuture(ping.cause()));
          } else {
            handler.handle(Future.succeededFuture(uri));
          }
          // connection is not needed anymore
          conn.close().onFailure(LOG::warn);
        });
      });
  }

  private void getMasterFromEndpoint(String endpoint, RedisSentinelConnectOptions options, Handler<AsyncResult<RedisURI>> handler) {
    // we can't use the endpoint as is, it should not contain a database selection,
    // but can contain authentication
    final RedisURI uri = new RedisURI(endpoint);
    connectionManager.getConnection(uri.baseUri(), null)
      .onFailure(err -> handler.handle(Future.failedFuture(err)))
      .onSuccess(conn -> {
        final String masterName = options.getMasterName();
        // Send a command just to check we have a working node
        conn.send(Request.cmd(Command.SENTINEL).arg("GET-MASTER-ADDR-BY-NAME").arg(masterName), getMasterAddrByName -> {
          // we don't need this connection anymore
          conn.close().onFailure(LOG::warn);

          if (getMasterAddrByName.failed()) {
            handler.handle(Future.failedFuture(getMasterAddrByName.cause()));
          } else {
            // Test the response
            final Response response = getMasterAddrByName.result();
            if (response == null) {
              handler.handle(Future.failedFuture("Failed to GET-MASTER-ADDR-BY-NAME " + masterName));
            } else {
              final String rHost = response.get(0).toString();
              final Integer rPort = response.get(1).toInteger();
              handler.handle(Future.succeededFuture(new RedisURI(uri, rHost.contains(":") ? "[" + rHost + "]" : rHost, rPort)));
            }
          }
        });
      });
  }

  private void getReplicaFromEndpoint(String endpoint, RedisSentinelConnectOptions options, Handler<AsyncResult<RedisURI>> handler) {
    // we can't use the endpoint as is, it should not contain a database selection,
    // but can contain authentication
    final RedisURI uri = new RedisURI(endpoint);
    connectionManager.getConnection(uri.baseUri(), null)
      .onFailure(err -> handler.handle(Future.failedFuture(err)))
      .onSuccess(conn -> {
        final String masterName = options.getMasterName();
        // Send a command just to check we have a working node
        conn.send(Request.cmd(Command.SENTINEL).arg("SLAVES").arg(masterName), sentinelReplicas -> {
          // connection is not needed anymore
          conn.close().onFailure(LOG::warn);

          if (sentinelReplicas.failed()) {
            handler.handle(Future.failedFuture(sentinelReplicas.cause()));
          } else {
            final Response response = sentinelReplicas.result();
            // Test the response
            if (response == null || response.size() == 0) {
              handler.handle(Future.failedFuture("No replicas linked to the master: " + masterName));
            } else {
              Response replicaInfoArr = response.get(RANDOM.nextInt(response.size()));
              if ((replicaInfoArr.size() % 2) > 0) {
                handler.handle(Future.failedFuture("Corrupted response from the sentinel"));
              } else {
                int port = 6379;
                String ip = null;

                if (replicaInfoArr.containsKey("port")) {
                  port = replicaInfoArr.get("port").toInteger();
                }

                if (replicaInfoArr.containsKey("ip")) {
                  ip = replicaInfoArr.get("ip").toString();
                }

                if (ip == null) {
                  handler.handle(Future.failedFuture("No IP found for a REPLICA node!"));
                } else {
                  final String host = ip.contains(":") ? "[" + ip + "]" : ip;

                  handler.handle(Future.succeededFuture(new RedisURI(uri, host, port)));
                }
              }
            }
          }
        });
      });
  }

}
