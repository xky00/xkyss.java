package com.xkyss.test.kafka;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ConsumerApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, Kafka Consumer!");

        Vertx vertx = Vertx.vertx();

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "my_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");
        config.put("acks", "1");

        // use consumer for interacting with Apache Kafka
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            System.out.println(
                " Processing key=" + record.key() +
                " ,value=" + record.value() +
                " ,partition=" + record.partition() +
                " ,offset=" + record.offset() +
                " ,time=" + System.currentTimeMillis());
        });

        // subscribe to several topics with list
        Set<String> topics = new HashSet<>();
        topics.add("topic1");
        topics.add("topic2");
        topics.add("topic3");
        consumer
            .subscribe(topics)
            .onSuccess(v -> {
                System.out.println("subscribed");
            })
            .onFailure(cause -> {
                System.out.println("Could not subscribe " + cause.getMessage());
            });

        System.in.read();
    }
}
