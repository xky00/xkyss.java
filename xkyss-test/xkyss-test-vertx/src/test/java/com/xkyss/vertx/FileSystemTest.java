package com.xkyss.vertx;

import io.vertx.core.Vertx;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

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
}
