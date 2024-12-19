package com.xkyss.redis.proxy;

import com.xkyss.redis.proxy.impl.RedisServerImpl;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Redis Proxy Server 接口
 */
public interface RedisServer {

    static RedisServer create(Vertx vertx, RedisServerOption options) {
        return new RedisServerImpl(vertx, options);
    }

    static RedisServer create(Vertx vertx) {
        return new RedisServerImpl(vertx, new RedisServerOption());
    }

    /**
     * Start the server listening for incoming connections using the specified options
     * through the constructor
     *
     * @return a {@code Future} completed with this server instance
     */
    Future<RedisServer> listen();

    /**
     * Start the server listening for incoming connections on the port and host specified
     *
     * @param port the port to listen on
     * @param host the host to listen on
     * @return a {@code Future} completed with this server instance
     */
    Future<RedisServer> listen(int port, String host);

    /**
     * Start the server listening for incoming connections on the port specified but on
     * "0.0.0.0" as host. It ignores any options specified through the constructor
     *
     * @param port the port to listen on
     * @return a {@code Future} completed with this server instance
     */
    Future<RedisServer> listen(int port);
}
