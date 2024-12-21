package com.xkyss.redis.proxy.middleware;

import com.xkyss.redis.proxy.RedisContext;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoteForwardHandler implements MiddlewareHandler<RedisContext> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteForwardHandler.class);

    private final Vertx vertx;
    private final RedisOptions options;
    private Redis redis;

    private boolean isConnected = false;

    public RemoteForwardHandler(Vertx vertx, RedisOptions options) {
        this.vertx = vertx;
        this.options = options;
    }

    @Override
    public Future<Void> handle(RedisContext ctx) {
        return checkConnect()
            .compose(v -> redis.send(ctx.getRequest()))
            .compose(r -> {
                logger.info("{}, {}", ctx.getRequest(), r.toString());
                return Future.succeededFuture();
            });
    }

    private Future<Void> checkConnect() {
        if (redis == null) {
            redis = Redis.createClient(vertx, options);
        }

        if (isConnected) {
            logger.info("Redis remove already connected.");
            return Future.succeededFuture();
        }

        Promise<Void> promise = Promise.promise();
        redis.connect()
            .onSuccess(r -> {
                logger.info("Connected to redis {} SUCCESS", options.getEndpoint());
                isConnected = true;
                promise.complete();
            })
            .onFailure(e -> {
                logger.error("Connected to redis {} FAILED", options.getEndpoint());
                isConnected = false;
                promise.fail(e);
            })
        ;
        return promise.future();
    }
}
