package com.xkyss.test.kafka;

import io.vertx.core.Vertx;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

import java.util.HashMap;
import java.util.Map;

public class ProducerApp {
    public static void main(String[] args)  {
        System.out.println("Hello, Kafka Producer!");

        Vertx vertx = Vertx.vertx();

        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        // use producer for interacting with Apache Kafka
        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);
        for (int i = 0; i < 5; i++) {
            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(String.format("topic%d", i), "message_" + i);
            System.out.println(String.format("%d   %d", i, System.currentTimeMillis()));
            producer.write(record);
        }

        // producer.close().onSuccess(v -> System.out.println("Producer closed!"));
    }
}
