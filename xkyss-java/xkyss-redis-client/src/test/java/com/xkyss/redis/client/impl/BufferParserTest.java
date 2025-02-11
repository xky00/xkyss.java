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

public class BufferParserTest {
    private static final Logger logger = LoggerFactory.getLogger(BufferParserTest.class);

    @Test
    public void testParseSimple() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("+PONG\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("+PONG\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }
}
