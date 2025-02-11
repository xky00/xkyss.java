package com.xkyss.redis.client.impl;

import com.xkyss.redis.client.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseTest {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTest.class);

    @Test
    public void testParseSimple() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger();

        VertxTestContext testContext = new VertxTestContext();

        final RESPParser parser = new RESPParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                logger.info(response.toString());
                if (counter.incrementAndGet() == 4) {
                    testContext.completeNow();
                }
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("+PONG\r\n+PONG\r\n+PONG\r\n+PONG\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }
}
