package com.xkyss.test.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaReadStream;

import java.util.Collections;

public class DashboardVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        // Get the Kafka consumer config
        JsonObject config = config();

        // Create the consumer
        KafkaReadStream<String, JsonObject> consumer = KafkaReadStream.create(vertx, config.getMap(), String.class, JsonObject.class);

        // Aggregates metrics in the dashboard
        consumer.handler(record -> {
            JsonObject obj = record.value();
            System.out.println(String.format("Receive %d: %d", obj.getString("Id"), System.currentTimeMillis()));
        });

        // Subscribe to Kafka
        consumer.subscribe(Collections.singleton("the_topic"));
    }

    @Override
    public void stop() throws Exception {

    }
}
