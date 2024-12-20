package com.xkyss.redis.proxy.middleware;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MiddlewareBuilder<TContext> {
    private final MiddlewareDelegate<TContext> fallback;
    private List<Function<MiddlewareDelegate<TContext>, MiddlewareDelegate<TContext>>> middlewares = new ArrayList<>();

    public MiddlewareBuilder() {
        this(ctx -> Future.succeededFuture());
    }

    public MiddlewareBuilder(MiddlewareDelegate<TContext> fallback) {
        Objects.requireNonNull(fallback);
        this.fallback = fallback;
    }

    public MiddlewareBuilder<TContext> newBuilder() {
        return new MiddlewareBuilder<>(fallback);
    }

    public MiddlewareDelegate<TContext> build() {
        MiddlewareDelegate<TContext> middlewareDelegate = fallback;
        for (Function<MiddlewareDelegate<TContext>, MiddlewareDelegate<TContext>> middleware : middlewares) {
            middlewareDelegate = middleware.apply(middlewareDelegate);
        }
        return middlewareDelegate;
    }

    public MiddlewareBuilder<TContext> use(Middleware<TContext> middleware) {
        return this.use(delegate -> context -> middleware.invoke(delegate, context));
    }

    public MiddlewareBuilder<TContext> use(Function<MiddlewareDelegate<TContext>, MiddlewareDelegate<TContext>> middleware) {
        this.middlewares.add(middleware);
        return this;
    }
}
