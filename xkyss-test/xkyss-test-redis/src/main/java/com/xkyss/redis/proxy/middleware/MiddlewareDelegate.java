package com.xkyss.redis.proxy.middleware;

import io.vertx.core.Future;

public interface MiddlewareDelegate<TContext> {
    Future<Void> handle(TContext ctx);
}
