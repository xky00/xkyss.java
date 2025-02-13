package com.xkyss.redis.client;

import com.xkyss.redis.client.impl.BufferParserTest;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RedisTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisTest.class);
    private static final String REDIS_HOST = "redis://localhost:6399";

    Vertx vertx = Vertx.vertx();

    @Test
    public void simpleTest() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        RedisOptions redisOptions = new RedisOptions().setConnectionString(REDIS_HOST).setDecodeWithBuffer(true);
        Redis client = Redis.createClient(vertx, redisOptions);

        client.connect()
            .onComplete(create -> {
                assertTrue(create.succeeded());
                final RedisConnection conn = create.result();

                conn.send(Request.cmd(Command.PING), send -> {
                    assertTrue(send.succeeded());
                    Assertions.assertNotNull(send.result());

                    Assertions.assertEquals("+PONG\r\n", send.result().toString());
                    testContext.completeNow();
                });
            })
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }
}
