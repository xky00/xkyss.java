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

    @Test
    public void testParseSimpleInChunks() throws InterruptedException {

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

        parser.handle(Buffer.buffer("+"));
        parser.handle(Buffer.buffer("P"));
        parser.handle(Buffer.buffer("O"));
        parser.handle(Buffer.buffer("N"));
        parser.handle(Buffer.buffer("G"));
        parser.handle(Buffer.buffer("\r"));
        parser.handle(Buffer.buffer("\n"));
        // ignore the last chunk
        parser.handle(Buffer.buffer("N"));
        parser.handle(Buffer.buffer("G"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseError() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("-ERROR\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("-ERROR\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseErrorInChunks() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("-ERROR\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("-"));
        parser.handle(Buffer.buffer("E"));
        parser.handle(Buffer.buffer("R"));
        parser.handle(Buffer.buffer("R"));
        parser.handle(Buffer.buffer("O"));
        parser.handle(Buffer.buffer("R"));
        parser.handle(Buffer.buffer("\r"));
        parser.handle(Buffer.buffer("\n"));
        // ignore the last chunk
        parser.handle(Buffer.buffer("O"));
        parser.handle(Buffer.buffer("R"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseIntegers() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(":1000\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(":1000\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseDoubles() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(",1000.123\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(",1000.123\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseBigNumbers() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("(3492890328409238509324850943850943825024385\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("(3492890328409238509324850943850943825024385\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseVerbatimStrings() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new BufferParserHandler() {
            @Override
            public void handle(Buffer buffer) {
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("=15\r\ntxt:Some string\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("=15\r\n"));
        parser.handle(Buffer.buffer("txt:Some string\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }
}
