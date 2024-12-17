package com.xkyss.redis.proxy;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;

public class RedisServerTest {

    Vertx vertx = Vertx.vertx();

    @Test
    public void listenTest() {
        RedisServerOption options = new RedisServerOption()
            .setHost("localhost")
            .setPort(6379);
        RedisServer server = RedisServer.create(this.vertx, options);

        VertxTestContext testContext = new VertxTestContext();
        server.listen().onComplete(ar -> {
            testContext.succeedingThenComplete();
        });
    }
}
