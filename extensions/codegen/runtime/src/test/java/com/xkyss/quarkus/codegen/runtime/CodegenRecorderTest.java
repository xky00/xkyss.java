package com.xkyss.quarkus.codegen.runtime;

import io.vertx.core.impl.verticle.PackageHelper;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


public class CodegenRecorderTest {

    @Test
    public void test_01() throws IOException {
        PackageHelper ph = new PackageHelper(Thread.currentThread().getContextClassLoader());

        List<JavaFileObject> javaFileObjects = Objects.requireNonNull(ph.find(this.getClass().getPackageName()));

        for (JavaFileObject o: javaFileObjects) {
            Path basePath = Paths.get(System.getProperty("user.dir"));
        }
    }
}
