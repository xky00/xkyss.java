package com.xkyss.redis.proxy;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class RedisServerTest {

    Vertx vertx = Vertx.vertx();

    @Test
    public void listenTest() throws InterruptedException {
        RedisServerOption options = new RedisServerOption()
            .setHost("localhost")
            .setPort(6479);
        RedisServer server = RedisServer.create(this.vertx, options);

        VertxTestContext testContext = new VertxTestContext();
        server.listen().onComplete(ar -> {
            testContext.succeedingThenComplete();
        });

        Assertions.assertTrue(testContext.awaitCompletion(200, TimeUnit.SECONDS));
    }
}
