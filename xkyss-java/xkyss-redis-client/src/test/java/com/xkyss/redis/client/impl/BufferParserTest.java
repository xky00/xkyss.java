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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
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

    @Test
    public void testParseBulkErrors() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("!21\r\nSYNTAX invalid syntax\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("!21\r\n"));
        parser.handle(Buffer.buffer("SYNTAX invalid syntax\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseBulkStrings() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("$6\r\nfoobar\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("$6\r\n"));
        parser.handle(Buffer.buffer("foobar\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseNullBulk() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("$-1\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("$-1\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseNull() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("_\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("_\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseBoolean() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger();
        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                int c = counter.incrementAndGet();
                if (c == 1) {
                    Assertions.assertArrayEquals("#t\r\n".getBytes(), buffer.getBytes());
                }
                else if (c == 2) {
                    Assertions.assertArrayEquals("#f\r\n".getBytes(), buffer.getBytes());
                    testContext.completeNow();
                }
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("#t\r\n"));
        parser.handle(Buffer.buffer("#f\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testMultiWithNull() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        String s = "*3\r\n$3\r\nfoo\r\n$-1\r\n$3\r\nbar\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseArray() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        String s = "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseArray2() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        String s = "*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";

        final RESPParser parser = new RESPParser(new ParserHandler() {
            @Override
            public void handle(Response buffer) {
                logger.info(buffer.toString());
                // Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseSet() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        String s = "~2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParsePush() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        String s = ">2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseMap() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals("%2\r\n+first\r\n:1\r\n+second\r\n:2\r\n".getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("%2\r\n"));
        parser.handle(Buffer.buffer("+first\r\n"));
        parser.handle(Buffer.buffer(":1\r\n"));
        parser.handle(Buffer.buffer("+second\r\n"));
        parser.handle(Buffer.buffer(":2\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseAttribute() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        final String s = "|1\r\n+key-popularity\r\n%2\r\n$1\r\na\r\n,0.1923\r\n$1\r\nb\r\n,0.0012\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer(s));
        // parser.handle(Buffer.buffer("|1\r\n"));
        // parser.handle(Buffer.buffer("+key-popularity\r\n"));
        // parser.handle(Buffer.buffer("%2\r\n"));
        // parser.handle(Buffer.buffer("$1\r\n"));
        // parser.handle(Buffer.buffer("a\r\n"));
        // parser.handle(Buffer.buffer(",0.1923\r\n"));
        // parser.handle(Buffer.buffer("$1\r\n"));
        // parser.handle(Buffer.buffer("b\r\n"));
        // parser.handle(Buffer.buffer(",0.0012\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseAttribute2() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        final RESPParser parser = new RESPParser(new ParserHandler() {
            @Override
            public void handle(Response buffer) {
                logger.info(buffer.toString());
                // Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("|1\r\n"));
        parser.handle(Buffer.buffer("+key-popularity\r\n"));
        parser.handle(Buffer.buffer("%2\r\n"));
        parser.handle(Buffer.buffer("$1\r\n"));
        parser.handle(Buffer.buffer("a\r\n"));
        parser.handle(Buffer.buffer(",0.1923\r\n"));
        parser.handle(Buffer.buffer("$1\r\n"));
        parser.handle(Buffer.buffer("b\r\n"));
        parser.handle(Buffer.buffer(",0.0012\r\n"));

        Assertions.assertFalse(testContext.awaitCompletion(100, TimeUnit.MICROSECONDS));
    }

    @Test
    public void testParseHello() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        final String s = "%7\r\n$6\r\nserver\r\n$5\r\nredis\r\n$7\r\nversion\r\n$5\r\n6.2.7\r\n$5\r\nproto\r\n:3\r\n$2\r\nid\r\n:18\r\n$4\r\nmode\r\n$10\r\nstandalone\r\n$4\r\nrole\r\n$6\r\nmaster\r\n$7\r\nmodules\r\n*0\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(buffer.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("%7\r\n"));
        parser.handle(Buffer.buffer("$6\r\n"));
        parser.handle(Buffer.buffer("server\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("redis\r\n"));
        parser.handle(Buffer.buffer("$7\r\n"));
        parser.handle(Buffer.buffer("version\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("6.2.7\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("proto\r\n"));
        parser.handle(Buffer.buffer(":3\r\n"));
        parser.handle(Buffer.buffer("$2\r\n"));
        parser.handle(Buffer.buffer("id\r\n"));
        parser.handle(Buffer.buffer(":18\r\n"));
        parser.handle(Buffer.buffer("$4\r\n"));
        parser.handle(Buffer.buffer("mode\r\n"));
        parser.handle(Buffer.buffer("$10\r\n"));
        parser.handle(Buffer.buffer("standalone\r\n"));
        parser.handle(Buffer.buffer("$4\r\n"));
        parser.handle(Buffer.buffer("role\r\n"));
        parser.handle(Buffer.buffer("$6\r\n"));
        parser.handle(Buffer.buffer("master\r\n"));
        parser.handle(Buffer.buffer("$7\r\n"));
        parser.handle(Buffer.buffer("modules\r\n"));
        parser.handle(Buffer.buffer("*0\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void testParseHello2() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        final String s = "%7\r\n$6\r\nserver\r\n$5\r\nredis\r\n$7\r\nversion\r\n$5\r\n6.2.7\r\n$5\r\nproto\r\n:3\r\n$2\r\nid\r\n:18\r\n$4\r\nmode\r\n$10\r\nstandalone\r\n$4\r\nrole\r\n$6\r\nmaster\r\n$7\r\nmodules\r\n*0\r\n";

        final RESPParser parser = new RESPParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                logger.info(response.toString());
                // Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("%7\r\n"));
        parser.handle(Buffer.buffer("$6\r\n"));
        parser.handle(Buffer.buffer("server\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("redis\r\n"));
        parser.handle(Buffer.buffer("$7\r\n"));
        parser.handle(Buffer.buffer("version\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("6.2.7\r\n"));
        parser.handle(Buffer.buffer("$5\r\n"));
        parser.handle(Buffer.buffer("proto\r\n"));
        parser.handle(Buffer.buffer(":3\r\n"));
        parser.handle(Buffer.buffer("$2\r\n"));
        parser.handle(Buffer.buffer("id\r\n"));
        parser.handle(Buffer.buffer(":18\r\n"));
        parser.handle(Buffer.buffer("$4\r\n"));
        parser.handle(Buffer.buffer("mode\r\n"));
        parser.handle(Buffer.buffer("$10\r\n"));
        parser.handle(Buffer.buffer("standalone\r\n"));
        parser.handle(Buffer.buffer("$4\r\n"));
        parser.handle(Buffer.buffer("role\r\n"));
        parser.handle(Buffer.buffer("$6\r\n"));
        parser.handle(Buffer.buffer("master\r\n"));
        parser.handle(Buffer.buffer("$7\r\n"));
        parser.handle(Buffer.buffer("modules\r\n"));
        parser.handle(Buffer.buffer("*0\r\n"));

        Assertions.assertTrue(testContext.awaitCompletion(100, TimeUnit.MICROSECONDS));
    }

    @Test
    public void testParseInfoKeyspace() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        final String s = "=81\r\n# Keyspace\r\ndb0:keys=18,expires=0,avg_ttl=0\r\ndb2:keys=1,expires=0,avg_ttl=0\r\n\r\n";

        final RESPBufferParser parser = new RESPBufferParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                Buffer buffer = response.toBuffer();
                logger.info(response.toString());
                Assertions.assertArrayEquals(s.getBytes(), buffer.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("=81\r\n"));
        parser.handle(Buffer.buffer("txt:# Keyspace\r\n")); // 14 + 2 :16
        parser.handle(Buffer.buffer("db0:keys=18,expires=0,avg_ttl=0\r\n")); // 31 + 2 :49
        parser.handle(Buffer.buffer("db2:keys=1,expires=0,avg_ttl=0\r\n")); // 30 + 2 :81
        parser.handle(Buffer.buffer("\r\n")); // BulkString tail

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void testParseInfoKeyspace2() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        final String s = "=81\r\ntxt:# Keyspace\r\ndb0:keys=18,expires=0,avg_ttl=0\r\ndb2:keys=1,expires=0,avg_ttl=0\r\n\r\n";

        final RESPParser parser = new RESPParser(new ParserHandler() {
            @Override
            public void handle(Response response) {
                logger.info(response.toString());
                // Assertions.assertArrayEquals(s.getBytes(), response.getBytes());
                testContext.completeNow();
            }

            @Override
            public void fail(Throwable t) {
                testContext.failNow(t);
            }
        }, 16);

        parser.handle(Buffer.buffer("=81\r\n"));
        parser.handle(Buffer.buffer("txt:# Keyspace\r\n")); // 14 + 2 :16
        parser.handle(Buffer.buffer("db0:keys=18,expires=0,avg_ttl=0\r\n")); // 31 + 2 :49
        parser.handle(Buffer.buffer("db2:keys=1,expires=0,avg_ttl=0\r\n")); // 30 + 2 :81
        parser.handle(Buffer.buffer("\r\n")); // BulkString tail

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }
}
