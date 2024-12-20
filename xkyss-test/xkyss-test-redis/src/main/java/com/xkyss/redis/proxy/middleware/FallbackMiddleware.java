package com.xkyss.redis.proxy.middleware;

import com.xkyss.redis.proxy.RedisContext;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FallbackMiddleware implements Middleware<RedisContext> {

    private static final Logger logger = LoggerFactory.getLogger(FallbackMiddleware.class);

    @Override
    public Future<Void> invoke(MiddlewareDelegate<RedisContext> next, RedisContext redisContext) {

        logger.info("FallbackMiddleware.");

        return Future.succeededFuture();
    }
}
