package com.xkyss.redis;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxTestContext;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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

    @Test
    public void test_scan_02() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .compose(r -> redis.scan(Arrays.asList("0", "MATCH", "*", "COUNT", "100")))
            .compose(r -> {
                Response response = r.get(1);
                List<String> collect = response.stream()
                    .map(Response::toString)
                    .collect(Collectors.toList());
                for (Object o : response) {
                    System.out.println(o.getClass() + ": " + o);
                }
                return Future.succeededFuture();
            })
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> System.out.println("ok: " + r))
            .onFailure(e -> System.out.println("fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void test_mget() throws InterruptedException {

        VertxTestContext testContext = new VertxTestContext();
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        client.connect()
            .compose(r -> redis.set(Arrays.asList("test:a", "11"))) // set test:a 11
            .compose(r -> redis.set(Arrays.asList("test:b", "22"))) // set test:b 22
            .compose(r -> redis.mget(Arrays.asList("test:a", "test:b"))) // mget test:a test:b
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> {
                System.out.println("ok: " + r);
                Assertions.assertTrue(r.isArray());
                Assertions.assertEquals(2, r.size());
                Assertions.assertEquals("11", r.get(0).toString());
                Assertions.assertEquals("22", r.get(1).toString());
            })
            .onFailure(e -> System.out.println("fail: " + e))
        ;

        Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
    }

    @Test
    public void test_compose() throws InterruptedException {
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        {
            VertxTestContext testContext = new VertxTestContext();
            client.connect()
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("connect ok: " + r))
                .onFailure(e -> System.out.println("connect fail: " + e))
            ;
            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }

        {
            String filename = "20230411-092730-425.rdb";
            VertxTestContext testContext = new VertxTestContext();
            redis.config(Arrays.asList("SET", "dbfilename", filename))
                .compose(r -> {
                    System.out.println("[STEP 1]: set dbfilename: " + r);
                    return Future.succeededFuture();
                })

                .compose(r -> {
                    System.out.println("[STEP 2]: save: " + r);
                    return redis.config(Arrays.asList("rewrite"));
                })
                .compose(r -> {
                    System.out.println("[STEP 3]: config rewrite: " + r);
                    return redis.scan(Arrays.asList("0", "MATCH", "mlcache:*"));
                })
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("ok: " + r.get(0)))
                .onFailure(e -> System.out.println("fail: " + e))
            ;

            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }
    }

    @Test
    public void test_get_dbfilename() throws InterruptedException {
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        {
            VertxTestContext testContext = new VertxTestContext();
            client.connect()
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("connect ok: " + r))
                .onFailure(e -> System.out.println("connect fail: " + e))
            ;
            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }

        {
            VertxTestContext testContext = new VertxTestContext();
            redis.config(Arrays.asList("GET", "dbfilename"))
                .compose(r -> {
                    System.out.println("[STEP 1]: get dbfilename: " + r);
                    System.out.println("[STEP 1]: get dbfilename: " + r.get("dbfilename"));
                    return Future.succeededFuture();
                })
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("ok: " + r))
                .onFailure(e -> System.out.println("fail: " + e))
            ;

            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }
    }

    @Test
    public void test_acl() throws InterruptedException {
        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        {
            VertxTestContext testContext = new VertxTestContext();
            client.connect()
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("connect ok: " + r))
                .onFailure(e -> System.out.println("connect fail: " + e))
            ;
            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }

        {
            VertxTestContext testContext = new VertxTestContext();
            redis.acl(Collections.singletonList("CAT"))
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("[acl CAT] ok: " + r))
                .onFailure(e -> System.out.println("connect fail: " + e))
            ;
            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }
    }


    @Test
    public void test_dbsize() throws InterruptedException {

        Redis client = Redis.createClient(vertx, REDIS_HOST);
        RedisAPI redis = RedisAPI.api(client);

        {
            VertxTestContext testContext = new VertxTestContext();
            client.connect()
                .onComplete(testContext.succeedingThenComplete())
                .onSuccess(r -> System.out.println("connect ok: " + r))
                .onFailure(e -> System.out.println("connect fail: " + e))
            ;
            Assertions.assertTrue(testContext.awaitCompletion(1, TimeUnit.SECONDS));
        }
    }
}
