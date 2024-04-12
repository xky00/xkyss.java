package com.xkyss.vertx;

import com.sun.org.apache.xml.internal.utils.StringComparable;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FileSystemTest {

    Vertx vertx = Vertx.vertx();

    @Test
    public void test_mkdirs() {
        Path p = Paths.get("D:/1/2/3/4/a.txt");
        vertx.fileSystem().mkdirs(p.getParent().toString(), ar -> {
            if (ar.succeeded()) {
                System.out.println("success");
            } else {
                System.out.println("fail");
            }
        });
    }

    @Test
    public void test_readDir() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        vertx.fileSystem().readDir("D:\\home\\mlcache\\mlcache-sample\\backup")
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(files -> {
                for (String file : files) {
                    if (file.endsWith(".backup")) {
                        System.out.println(file);

                        Path fileName = Paths.get(file).getFileName();
                        String fileNameNoExtension = fileName.toString().substring(0, fileName.toString().lastIndexOf("."));

                        try {
                            // 初始时间
                            long t1 = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").parse(fileNameNoExtension).getTime();
                            // 有效时长 (s)
                            long duration = 1200;
                            // 当前时间
                            long now = System.currentTimeMillis();

                            System.out.println("d: " + (now - t1) + " delete?: " + (now - t1 > duration * 1000));

                            // if (now - t1 > duration * 1000) {
                                // System.out.println("d: " + (now - t1) + " delete " + file);
                                // vertx.fileSystem().delete(file);
                            // }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            })
            .onFailure(v -> System.out.println("Caffeine cleanUp failed" + v));

        testContext.awaitCompletion(20, TimeUnit.SECONDS);
    }

    @Test
    public void test_read_latest_file() throws InterruptedException {
        // 文件内容
        // {"default":{"CacheDemo:Service:":{"111":{"value":"111 @ Fri Apr 12 14:46:52 CST 2024","expireTime":1712904592117,"accessTime":1712904412123}}}}

        VertxTestContext testContext = new VertxTestContext();
        vertx.fileSystem().readDir("D:\\home\\mlcache\\mlcache-sample\\backup\\caffeine")
            .compose(files -> {
                if (files == null || files.isEmpty()) {
                    return null;
                }

                Collections.sort(files, Comparator.reverseOrder());

                return Future.succeededFuture(files.get(0));
            })
            .compose(file -> {
                System.out.println(file);
                return vertx.fileSystem().readFile(file);
            })
            .compose(buffer -> {
                JsonObject o = (JsonObject) Json.decodeValue(buffer);
                return Future.succeededFuture(o);
            })
            .onComplete(testContext.succeedingThenComplete())
            .onSuccess(r -> {
                System.out.println("文件内容: " + r);
                Map<String, Object> map = r.getMap();
                for (Map.Entry<String, Object> entry0 : map.entrySet()) {
                    String area = entry0.getKey();
                    System.out.println("\tArea: " + area);
                    for (Map.Entry<String, Object> entry1 : ((Map<String, Object>) entry0.getValue()).entrySet()) {
                        System.out.println("\t\tCacheName: " + entry1.getKey());

                        for (Map.Entry<String, Object> entry2 : ((Map<String, Object>) entry1.getValue()).entrySet()) {
                            System.out.println("\t\t\tKey: " + entry2.getKey());

                            for (Map.Entry<String, Object> entry3 : ((Map<String, Object>) entry2.getValue()).entrySet()) {
                                System.out.println("\t\t\t\t" + entry3.getKey() + ": " + entry3.getValue());
                            }
                        }
                    }
                }
            });

        testContext.awaitCompletion(10, TimeUnit.SECONDS);
    }

    static class Info {
        Map<String, Area> areas;
    }

    static class Area {
        Map<String, CacheName> cacheNames;
    }

    static class CacheName {
        Map<String, String> caches;
    }
}
