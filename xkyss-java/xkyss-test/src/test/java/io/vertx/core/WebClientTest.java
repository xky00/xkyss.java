package io.vertx.core;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class WebClientTest {
    @Test
    public void test_01() throws ExecutionException, InterruptedException {
        Vertx vertx = Vertx.vertx();
        WebClient client = WebClient.create(vertx);
        JsonObject body = client.get("dog.ceo", "/api/breeds/image/random")
            .as(BodyCodec.jsonObject())
            .send()
            .toCompletionStage()
            .toCompletableFuture()
            .get()
            .body();
        Assertions.assertNotNull(body);
    }
}
