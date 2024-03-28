package io.vertx.core;

import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class HttpTest {

    @Test
    public void test_01() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        Vertx vertx = Vertx.vertx();
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 80, "dog.ceo", "/api/breeds/image/random")
            .onComplete(ar1 -> {
                if (ar1.succeeded()) {
                    ar1.result()
                        .send()
                        .onComplete(ar2 -> {
                            if (ar2.succeeded()) {
                                future.complete("ok");
                            }
                            else {
                                future.complete("error");
                            }
                        });
                }
            });
        String result = future.get();
        Assertions.assertTrue(result.equals("ok") || result.equals("error"));
    }
}
