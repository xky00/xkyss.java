package com.xkyss.vertx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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

    @Test
    public void test_02() throws ExecutionException, InterruptedException, TimeoutException {
        Vertx vertx = Vertx.vertx();

        List<Future<HttpResponse<JsonObject>>> futures = new ArrayList<>();
        List<Server> servers = new ArrayList<>();
        servers.add(new Server("192.168.1.38", 8083));
        servers.add(new Server("192.168.1.38", 8085));

        for (Server server : servers) {
            Future<HttpResponse<JsonObject>> future = WebClient.create(vertx)
                .get(server.getHostPort(), server.getHostIp(), "/sdk/cache/local")
                .addQueryParam("area", "default")
                .addQueryParam("cacheName", "CacheDemo:Service:")
                .addQueryParam("key", "113")
                .as(BodyCodec.jsonObject())
                .send()
                .onSuccess(r -> {
                    System.out.println("  Server: " + server);
                    System.out.println("Response: " + r.body());
                })
                .onFailure(e -> System.out.println(e))
                .recover(e -> {
                    return Future.succeededFuture();
                });

            futures.add(future);
        }

        Future.all(futures).toCompletionStage().toCompletableFuture().get(5000, TimeUnit.MILLISECONDS);
    }

    static class Server {
        private String hostIp;
        private int hostPort;

        public Server(String hostIp, int hostPort) {
            this.hostIp = hostIp;
            this.hostPort = hostPort;
        }

        @Override
        public String toString() {
            return JsonObject.of("hostIp", hostIp, "hostPort", hostPort).toString();
        }

        public String getHostIp() {
            return hostIp;
        }

        public void setHostIp(String hostIp) {
            this.hostIp = hostIp;
        }

        public int getHostPort() {
            return hostPort;
        }

        public void setHostPort(int hostPort) {
            this.hostPort = hostPort;
        }
    }
}
