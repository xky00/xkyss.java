package com.xkyss.vertx;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FutureTest {

    @Test
    public void test_01() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        Future<Integer> future = Future.succeededFuture(100);
        future.onComplete(r -> {
            System.out.println(r.result());
            testContext.succeedingThenComplete();
        });

        testContext.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    public void test_02() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();

        CompositeFuture cf = Future.join(Future.succeededFuture(1), Future.succeededFuture(2));
        cf = Future.join(cf, Future.succeededFuture(3));
        cf.onComplete(r -> {
            System.out.println(r.result());
            testContext.succeedingThenComplete();
        });
        testContext.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    public void test_03() throws InterruptedException {
        VertxTestContext testContext = new VertxTestContext();
        CompositeFuture cf = Future.all(Future.succeededFuture(1), Future.succeededFuture(2));
        cf.onSuccess(r -> {
            List<Integer> list = r.list();
            Assertions.assertTrue(list.contains(1));
            Assertions.assertTrue(list.contains(2));
            testContext.succeedingThenComplete();
        });

        testContext.awaitCompletion(1, TimeUnit.SECONDS);
    }

    @Test
    public void test_map() {
        Future<String> f1 = Future.succeededFuture(1).compose(i -> Future.succeededFuture(String.format("hello %d", i)));
    }
}
