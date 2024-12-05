package com.xkyss.redis;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("LoggingSimilarMessage")
public class KeysTest {

    // private static final String REDIS_HOST = "redis://localhost:6399?db=5";
    private static final String REDIS_HOST = "redis://192.168.1.47:6399?db=11";
    private static final Logger logger = LoggerFactory.getLogger(KeysTest.class);

    Vertx vertx = Vertx.vertx();

    @Test
    public void test_log() {
        logger.info("REDIS_HOST: {}", REDIS_HOST);
    }

    @Test
    public void test_scan() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> logger.info("connect ok: {}", r.toString()))
            .onFailure(e -> logger.error("connect fail: ", e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));

        redis.scan(Arrays.asList("0", "MATCH", "*", "COUNT", "100"), r -> {
            if (r.succeeded()) {
                logger.info("scan ok, size: {}", r.result().get(1).size());
            } else {
                logger.error("scan fail", r.cause());
            }
            testContext.succeedingThenComplete();
        });
        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void test_keys() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> logger.info("connect ok: {}", r.toString()))
            .onFailure(e -> logger.error("connect fail: ", e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));

        redis.keys("*")
            .compose(r -> {
                System.out.println("keys ok: " + r.size());
                // for (int i = 0; i < r.size(); i++) {
                //     logger.info("keys {}: {}", i+1, r.get(i));
                // }

                try {
                    List<String> keys = r.stream().limit(10).map(Response::toString).collect(Collectors.toList());
                    return redis.mget(keys);
                }
                catch (Exception e) {
                    throw new RuntimeException("keys failed", e);
                }
            })
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> {
                try {
                    Object d = decode(r.get(0).toBytes(), true);
                    logger.info("mget ok: {}", d);
                } catch (Exception e) {
                    logger.error("decode fail: ", e);
                }
            })
            .onFailure(e -> logger.error("connect fail: ", e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }
    public static Object decode(byte[] buffer, boolean useIdentityNumber) throws Exception {
        ByteArrayInputStream in;
        if (useIdentityNumber) {
            in = new ByteArrayInputStream(buffer, 4, buffer.length - 4);
        } else {
            in = new ByteArrayInputStream(buffer);
        }
        ObjectInputStream ois = new ObjectInputStream(in);
        return ois.readObject();
    }
}
