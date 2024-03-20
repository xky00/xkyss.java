package io.vertx.core;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonTest {
    @Test
    public void test_01() {
        JsonObject jo = JsonObject.of("a", 1);
        Assertions.assertEquals(1, jo.getInteger("a"));
    }

    @Test
    public void test_02() {
        Vertx vertx = Vertx.vertx();
        EventBus eventBus = vertx.eventBus();
        MessageConsumer<String> consumer = eventBus.consumer("news.uk.sport");
        consumer.handler(message -> {
            System.out.println("I have received a message: " + message.body());
            message.reply("how interesting!");
        });
        eventBus
            .request("news.uk.sport", "Yay! Someone kicked a ball across a patch of grass")
            .onComplete(ar -> {
                if (ar.succeeded()) {
                    System.out.println("Received reply: " + ar.result().body());
                }
            });
    }
}
