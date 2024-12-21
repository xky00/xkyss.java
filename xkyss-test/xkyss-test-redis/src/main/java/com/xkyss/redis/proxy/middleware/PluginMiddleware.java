package com.xkyss.redis.proxy.middleware;

import com.xkyss.redis.proxy.RedisContext;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginMiddleware implements Middleware<RedisContext> {

    private static final Logger logger = LoggerFactory.getLogger(PluginMiddleware.class);

    @Override
    public Future<Void> invoke(MiddlewareHandler<RedisContext> next, RedisContext ctx) {
        logger.info("PluginMiddleware.invoke");

        return before(ctx)
            .compose(v -> {
                logger.info("PluginMiddleware.invoke 1");
                return next.handle(ctx);
            })
            .compose(v -> {
                logger.info("PluginMiddleware.after 1");
                return after(ctx);
            });
    }

    private Future<Void> before(RedisContext ctx) {
        logger.info("PluginMiddleware.before 0");
        return Future.future(p -> {
            logger.info("PluginMiddleware.before");
            p.complete();
        });
    }

    private Future<Void> after(RedisContext ctx) {
        logger.info("PluginMiddleware.after 0");
        return Future.future(p -> {
            logger.info("PluginMiddleware.after");
            p.complete();
        });
    }
}
