package com.xkyss.redis.proxy;

import com.xkyss.redis.proxy.impl.RedisServerImpl;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * Redis Proxy Server 接口
 */
public interface RedisServer {

    static RedisServer create(Vertx vertx, RedisServerOptions options) {
        return new RedisServerImpl(vertx, options);
    }

    static RedisServer create(Vertx vertx) {
        return new RedisServerImpl(vertx, new RedisServerOptions());
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

    /**
     * Set the endpoint handler for the server. If an Redis client connect to the server a
     * new RedisEndpoint instance will be created and passed to the handler
     *
     * @param handler the endpoint handler
     * @return a reference to this, so the API can be used fluently
     */
    RedisServer endpointHandler(Handler<RedisEndpoint> handler);

    /**
     * Set an exception handler for the server, that will be called when an error happens independantly of an
     * accepted {@link RedisEndpoint}, like a rejected connection
     *
     * @param handler the exception handler
     * @return a reference to this, so the API can be used fluently
     */
    RedisServer exceptionHandler(Handler<Throwable> handler);
}
