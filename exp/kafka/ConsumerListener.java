///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.vertx:vertx-kafka-client:4.5.11


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.kafka.client.consumer.KafkaConsumer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ConsumerListener extends AbstractVerticle {

    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ConsumerListener());
    }

    @Override
    public void start() throws Exception {
        System.out.println(ConsumerListener.class.getName() + " start.");
        KafkaConsumer<String, String> consumer = newConsumer(getVertx());
    }

    @Override
    public void stop() throws Exception {
        System.out.println(ConsumerListener.class.getName() + " stopped.");
    }

    public io.vertx.kafka.client.consumer.KafkaConsumer<String, String> newConsumer(Vertx vertx) {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "192.168.1.47:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("group.id", "my_group");
        config.put("auto.offset.reset", "latest");
        config.put("enable.auto.commit", "false");

        // use consumer for interacting with Apache Kafka
        io.vertx.kafka.client.consumer.KafkaConsumer<String, String> consumer = io.vertx.kafka.client.consumer.KafkaConsumer.create(vertx, config);

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
