package com.xkyss.redis.proxy.middleware;

import com.xkyss.redis.proxy.RedisContext;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class MiddlewareBuilder<TContext> {
    private static final Logger logger = LoggerFactory.getLogger(MiddlewareBuilder.class);

    private final MiddlewareDelegate<TContext> fallback;
    private List<Function<MiddlewareDelegate<TContext>, MiddlewareDelegate<TContext>>> middlewares = new ArrayList<>();

    public MiddlewareBuilder() {
        this(ctx -> Future.future(p -> {
            logger.info("MiddlewareBuilder fallback");
            p.complete();
        }));
    }

    public MiddlewareBuilder(MiddlewareDelegate<TContext> fallback) {
        Objects.requireNonNull(fallback);
        this.fallback = fallback;
    }

    public MiddlewareBuilder<TContext> newBuilder() {
        return new MiddlewareBuilder<>(fallback);
    }

    public MiddlewareDelegate<TContext> build() {
        MiddlewareDelegate<TContext> delegate = fallback;
        for (int i = middlewares.size() - 1; i >=0; i--) {
            delegate = middlewares.get(i).apply(delegate);
        }
        return delegate;
    }

    public MiddlewareBuilder<TContext> use(Middleware<TContext> middleware) {
        return this.use(delegate -> context -> middleware.invoke(delegate, context));
    }

    public MiddlewareBuilder<TContext> use(Function<MiddlewareDelegate<TContext>, MiddlewareDelegate<TContext>> middleware) {
        this.middlewares.add(middleware);
        return this;
    }

    public MiddlewareDelegate<RedisContext> demo1() {
        MiddlewareDelegate<RedisContext> fb = ctx0 -> {
            return Future.succeededFuture();
        };

        Function<MiddlewareDelegate<RedisContext>, MiddlewareDelegate<RedisContext>> m1 = d1 -> {
            return ctx1 -> {
                FallbackMiddleware mw = new FallbackMiddleware();
                return mw.invoke(d1, ctx1);
            };
        };

        Function<MiddlewareDelegate<RedisContext>, MiddlewareDelegate<RedisContext>> m2 = d2 -> {
            return ctx2 -> {
                PluginMiddleware mw = new PluginMiddleware();
                return mw.invoke(d2, ctx2);
            };
        };

        MiddlewareDelegate<RedisContext> r = m2.apply(m1.apply(fb));
        return r;
    }

    public MiddlewareDelegate<RedisContext> demo2() {
        MiddlewareDelegate<RedisContext> d1 = ctx0 -> {
            return Future.succeededFuture();
        };

        FallbackMiddleware mw1 = new FallbackMiddleware();
        MiddlewareDelegate<RedisContext> d2 = ctx1 -> mw1.invoke(d1, ctx1);

        PluginMiddleware mw2 = new PluginMiddleware();
        MiddlewareDelegate<RedisContext> d3 = ctx2 -> mw2.invoke(d2, ctx2);

        return d3;
    }

    public MiddlewareDelegate<RedisContext> demo3() {
        MiddlewareDelegate<RedisContext> d3 = ctx2 -> {

            FallbackMiddleware mw1 = new FallbackMiddleware();
            return mw1.invoke(ctx1 -> {

                PluginMiddleware mw2 = new PluginMiddleware();
                return mw2.invoke(ctx0 -> {
                    logger.info("MiddlewareBuilder demo3");
                    return Future.succeededFuture();
                }, ctx1);

            }, ctx2);
        };

        return d3;
    }
}
