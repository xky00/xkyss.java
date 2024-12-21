package com.xkyss.redis.proxy;

import com.xkyss.redis.proxy.middleware.FallbackMiddleware;
import com.xkyss.redis.proxy.middleware.MiddlewareBuilder;
import com.xkyss.redis.proxy.middleware.PluginMiddleware;
import com.xkyss.redis.proxy.middleware.RemoteForwardHandler;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import io.vertx.redis.client.RedisOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RedisServerTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisServerTest.class);

    Vertx vertx = Vertx.vertx();

    @Test
    public void listenTest() throws InterruptedException {
        RedisServerOptions options = new RedisServerOptions()
            .setHost("localhost")
            .setPort(6479);
        RemoteForwardHandler remoteForwardHandler = new RemoteForwardHandler(vertx, new RedisOptions());
        RedisServer server = RedisServer.create(this.vertx, options)
            .endpointHandler(endpoint -> {
                logger.info("endpoint: {}", endpoint);
            })
            .middlewareBuilder(() -> new MiddlewareBuilder<RedisContext>(remoteForwardHandler)
                .use(new PluginMiddleware())
                .use(new FallbackMiddleware())
                .build());

        VertxTestContext testContext = new VertxTestContext();
        server.listen()
            .onFailure(t -> {
                logger.error("listen FAILED.", t);
                testContext.failNow(t);
            })
            .onComplete(ar -> {
                testContext.succeedingThenComplete();
            });

        Assertions.assertTrue(testContext.awaitCompletion(200, TimeUnit.SECONDS));
    }
}
