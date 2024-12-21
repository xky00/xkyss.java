package com.xkyss.redis.proxy.middleware;

import io.vertx.core.Future;

/**
 * 中间件
 * @param <TContext>
 */
public interface Middleware<TContext> {

    /**
     * 调用
     * @param next 下一个
     * @param context 上下文
     * @return Future
     */
    Future<Void> invoke(MiddlewareHandler<TContext> next, TContext context);
}
