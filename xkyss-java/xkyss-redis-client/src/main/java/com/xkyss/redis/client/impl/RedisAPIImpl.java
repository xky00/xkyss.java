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

import com.xkyss.redis.client.*;
import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class RedisAPIImpl implements RedisAPI {

  private static final Logger LOG = LoggerFactory.getLogger(RedisAPIImpl.class);

  private final Redis redis;
  private final RedisConnection connection;

  public RedisAPIImpl(RedisConnection connection) {
    this.connection = connection;
    this.redis = null;
  }

  public RedisAPIImpl(Redis redis) {
    this.connection = null;
    this.redis = redis;
  }

  @Override
  public Future<Response> send(Command cmd, String... args) {
    final Request req = Request.cmd(cmd);

    if (args != null) {
      for (String o : args) {
        if (o == null) {
          req.nullArg();
        } else {
          req.arg(o);
        }
      }
    }

    if (redis != null) {
      // operating in pooled mode
      return redis.send(req);
    } else if (connection != null) {
      // operating on connection mode
      return connection.send(req);
    }

    return Future.failedFuture(new IllegalStateException("Invalid state: no pool or connection available"));
  }

  @Override
  public void close() {
    if (redis != null) {
      // operating in pooled mode
      redis.close();
    } else if (connection != null) {
      // operating on connection mode
      connection.close().onFailure(LOG::warn);
    }
  }
}
