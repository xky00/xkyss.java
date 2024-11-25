package io.vertx.kafka;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MlCacheTest {

    @Test
    public void test_01() throws IOException {
        Vertx vertx = Vertx.vertx();

        KafkaConsumer<String, String> consumer = newConsumer(vertx);

        System.in.read();
    }


    public KafkaConsumer<String, String> newConsumer(Vertx vertx) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "192.168.1.47:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "my_group");
        config.put("auto.offset.reset", "latest");
        config.put("enable.auto.commit", "false");

        // use consumer for interacting with Apache Kafka
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            System.out.println("Processing key=" + record.key() + ",value=" + record.value() +
                ",partition=" + record.partition() + ",offset=" + record.offset());
        });

        // subscribe to several topics with list
        Set<String> topics = new HashSet<>();
        topics.add("mlcache-put_confirm");
        // topics.add("topic2");
        // topics.add("topic3");
        consumer
            .subscribe(topics)
            .onSuccess(v -> {
                System.out.println("subscribed");
            })
            .onFailure(cause -> {
                System.out.println("Could not subscribe " + cause.getMessage());
            });

        return consumer;
    }
}
