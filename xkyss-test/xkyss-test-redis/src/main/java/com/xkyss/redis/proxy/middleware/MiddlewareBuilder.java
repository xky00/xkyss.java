package com.xkyss.redis.proxy.middleware;

import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MiddlewareBuilder<TContext> {
    private static final Logger logger = LoggerFactory.getLogger(MiddlewareBuilder.class);

    private final MiddlewareHandler<TContext> fallback;
    private List<Function<MiddlewareHandler<TContext>, MiddlewareHandler<TContext>>> middlewares = new ArrayList<>();

    public MiddlewareBuilder() {
        this(ctx -> Future.future(p -> {
            logger.info("MiddlewareBuilder fallback");
            p.complete();
        }));
    }

    public MiddlewareBuilder(MiddlewareHandler<TContext> fallback) {
        Objects.requireNonNull(fallback);
        this.fallback = fallback;
    }

    public MiddlewareBuilder<TContext> newBuilder() {
        return new MiddlewareBuilder<>(fallback);
    }

    public MiddlewareHandler<TContext> build() {
        MiddlewareHandler<TContext> delegate = fallback;
        for (int i = middlewares.size() - 1; i >=0; i--) {
            delegate = middlewares.get(i).apply(delegate);
        }
        return delegate;
    }

    public MiddlewareBuilder<TContext> use(Middleware<TContext> middleware) {
        return this.use(delegate -> context -> middleware.invoke(delegate, context));
    }

    public MiddlewareBuilder<TContext> use(Function<MiddlewareHandler<TContext>, MiddlewareHandler<TContext>> middleware) {
        this.middlewares.add(middleware);
        return this;
    }
}
