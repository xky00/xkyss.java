package io.vertx.kafka;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.admin.KafkaAdminClient;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

public class KafkaTest {

    @Test
    public void test_01() throws IOException, InterruptedException {
        Vertx vertx = Vertx.vertx();

        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        KafkaAdminClient adminClient = KafkaAdminClient.create(vertx, config);
        adminClient.listTopics().onSuccess(topics ->
            System.out.println("Topics= " + topics)
        );

        KafkaConsumer<String, String> consumer = newConsumer(vertx);
        KafkaProducer<String, String> producer = newProducer(vertx);

        for (int i = 0; i < 5; i++) {
            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(String.format("topic%d", i), "message_" + i);
            producer.write(record);
        }

        System.in.read();
    }

    @Test
    public void test_02() {
        Vertx vertx = Vertx.vertx();
        KafkaProducer<String, String> producer = newProducer(vertx);

        for (int i = 0; i < 5; i++) {
            // only topic and message value are specified, round robin on destination partitions
            KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(String.format("topic%d", i), "message_" + i);
            producer.write(record);
        }
    }

    public KafkaConsumer<String, String> newConsumer(Vertx vertx) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "my_group");
        config.put("auto.offset.reset", "earliest");
        config.put("enable.auto.commit", "false");

        // use consumer for interacting with Apache Kafka
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config);

        consumer.handler(record -> {
            System.out.println("Processing key=" + record.key() + ",value=" + record.value() +
                ",partition=" + record.partition() + ",offset=" + record.offset());
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

        return consumer;
    }

    private KafkaProducer<String, String> newProducer(Vertx vertx) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        // use producer for interacting with Apache Kafka
        KafkaProducer<String, String> producer = KafkaProducer.create(vertx, config);
        return producer;
    }
}
