package com.xkyss.redis;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RedisTest {

    private static final String REDIS_HOST = "redis://localhost:6399";

    Vertx vertx = Vertx.vertx();

    @Test
    public void test_bgsave_01() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
            ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));

        redis.bgsave(Collections.singletonList("SCHEDULE"), r -> {
            System.out.println("bgsave ok: " + r.result());
        });
    }

    @Test
    public void test_bgsave_02() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .compose(conn -> redis.bgsave(Collections.singletonList("SCHEDULE")))
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("bgsave ok: " + r))
            .onFailure(e -> System.out.println("bgsave fail: " + e))
        ;
        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void test_dbfilename() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));

        redis.config(Arrays.asList("GET", "dbfilename"), r -> {
            testContext.succeedingThenComplete();
            if (r.succeeded()) {
                System.out.println("dbfilename ok: " + r.result());
            } else {
                System.out.println("dbfilename fail: " + r.cause());
            }
        });

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void test_dbfilename_02() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));

        redis
            .config(Arrays.asList("SET", "dbfilename", String.format("%d.rdb", System.currentTimeMillis())), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 1]: set dbfilename ok: " + r.result());
                } else {
                    System.out.println("[STEP 1]:set dbfilename fail: " + r.cause());
                }
            })
            .bgsave(Arrays.asList("SCHEDULE"), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 2]: bgsave ok: " + r.result());
                } else {
                    System.out.println("[STEP 2]: bgsave fail: " + r.cause());
                }
            })
            .config(Arrays.asList("SET", "dbfilename", "dump.rdb"), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 3]: set dbfilename ok: " + r.result());
                } else {
                    System.out.println("[STEP 3]: set dbfilename fail: " + r.cause());
                }
                testContext.succeedingThenComplete();
            });

        Assertions.assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }


    @Test
    public void test_rewrite() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));

        String timeStr = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date(System.currentTimeMillis()));
        redis
            .config(Arrays.asList("SET", "dbfilename", String.format("%s.rdb", timeStr)), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 1]: set dbfilename ok: " + r.result());
                } else {
                    System.out.println("[STEP 1]:set dbfilename fail: " + r.cause());
                }
            })
            .bgsave(Arrays.asList("SCHEDULE"), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 2]: bgsave ok: " + r.result());
                } else {
                    System.out.println("[STEP 2]: bgsave fail: " + r.cause());
                }
            })
            .config(Arrays.asList("rewrite"), r -> {
                if (r.succeeded()) {
                    System.out.println("[STEP 3]: rewrite ok: " + r.result());
                } else {
                    System.out.println("[STEP 3]: rewrite fail: " + r.cause());
                }
                testContext.succeedingThenComplete();
            });

        Assertions.assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @Test
    public void test_config_get_dir() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));

        redis.config(Arrays.asList("GET", "dir"), r -> {
            if (r.succeeded()) {
                System.out.println("get dir ok: " + r.result().get("dir").toString());
            } else {
                System.out.println("get dir fail: " + r.cause());
            }
            testContext.succeedingThenComplete();
        });

        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void test_scan() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));

        redis.scan(Arrays.asList("0", "MATCH", "*", "COUNT", "100"), r -> {
            if (r.succeeded()) {
                System.out.println("scan ok: " + r.result().get(1).size());
            } else {
                System.out.println("scan fail: " + r.cause());
            }
            testContext.succeedingThenComplete();
        });
        Assertions.assertTrue(testContext.awaitCompletion(2, TimeUnit.SECONDS));
    }

    @Test
    public void test_save_02() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("connect ok: " + r))
            .onFailure(e -> System.out.println("connect fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));

        VertxTestContext testContext2 = new VertxTestContext();
        redis.save(r -> {
            if (r.succeeded()) {
                System.out.println("save ok: " + r.result());
                // TODO: 无法判断容器宿主的文件目录
                boolean b = vertx.fileSystem().existsBlocking("D:\\Code\\thzt\\mlcache\\doc\\docker\\mlcache-dashboard-dev\\data\\redis\\data\\20240411-092730-325.rdb");
                System.out.println("\t file exists: " + b);
            } else {
                System.out.println("save fail: " + r.cause());
            }
            testContext2.succeedingThenComplete();
        });

        // TODO: 会返回false
        testContext2.awaitCompletion(1, TimeUnit.SECONDS);
    }
}
