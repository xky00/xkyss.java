package com.xkyss.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class RestClientTest {
    @Test
    public void test_01() throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();

        AtomicInteger step = new AtomicInteger(0);
        JsonObject body = WebClient.create(vertx)
            .get(8080, "localhost", "/products/prod3568")
            .as(BodyCodec.jsonObject())
            .send()
            .onSuccess(v -> {
                Assertions.assertEquals(0, step.getAndIncrement());
            })
            .toCompletionStage()
            .toCompletableFuture()
            .get()
            .body();

        Assertions.assertEquals(1, step.getAndIncrement());

        Assertions.assertNotNull(body);
        Assertions.assertEquals("prod3568", body.getString("id"));
    }
}
