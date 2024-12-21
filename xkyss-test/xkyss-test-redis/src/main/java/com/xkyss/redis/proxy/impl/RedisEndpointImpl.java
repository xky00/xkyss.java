package com.xkyss.redis.proxy.impl;

import com.xkyss.redis.proxy.RedisContext;
import com.xkyss.redis.proxy.RedisEndpoint;
import com.xkyss.redis.proxy.middleware.MiddlewareHandler;
import io.vertx.core.Handler;
import io.vertx.core.net.impl.NetSocketInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisEndpointImpl implements RedisEndpoint {

    private static final Logger log = LoggerFactory.getLogger(RedisEndpointImpl.class);
    private final NetSocketInternal so;
    private final MiddlewareHandler<RedisContext> middleware;

    // handler to call when the endpoint is isClosed
    private Handler<Void> closeHandler;
    // handler to call when a problem at protocol level happens
    private Handler<Throwable> exceptionHandler;

    private boolean isConnected;
    private boolean isClosed;

    public RedisEndpointImpl(NetSocketInternal so, MiddlewareHandler<RedisContext> middleware) {
        this.so = so;
        this.middleware = middleware;
        this.isConnected = true;
    }

    public boolean isConnected() {
        synchronized (this.so) {
            return this.isConnected;
        }
    }

    public boolean isClosed() {
        synchronized (this.so) {
            return this.isClosed;
        }
    }

    /**
     * Check if the connection was accepted
     */
    private void checkConnected() {
        if (!this.isConnected) {
            throw new IllegalArgumentException("Connection not accepted yet");
        }
    }

    /**
     * Check if the Redis endpoint is closed
     */
    private void checkClosed() {
        if (this.isClosed) {
            throw new IllegalArgumentException("Redis endpoint is closed");
        }
    }

    /**
     * Used for calling the exception handler when an error at protocol level happens
     *
     * @param t exception raised
     */
    void handleException(Throwable t) {
        synchronized (this.so) {
            if (this.exceptionHandler != null) {
                this.exceptionHandler.handle(t);
            }
        }
    }

    /**
     * Used for calling the close handler when the remote Redis client closes the connection
     */
    void handleClosed() {
        synchronized (this.so) {
            this.cleanup();

            if (this.closeHandler != null) {
                this.closeHandler.handle(null);
            }
        }
    }

    /**
     * Cleanup
     */
    private void cleanup() {
        if (!this.isClosed) {
            this.isClosed = true;
            this.isConnected = false;
        }
    }

    @Override
    public void handle(RedisContext rc) {
        this.middleware.handle(rc).onComplete(v -> {
            log.info("handle complete.");
        });
    }
}
