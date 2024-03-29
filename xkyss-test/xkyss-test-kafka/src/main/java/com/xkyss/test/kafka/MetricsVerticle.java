package com.xkyss.test.kafka;

import com.sun.management.OperatingSystemMXBean;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.producer.KafkaWriteStream;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.lang.management.ManagementFactory;
import java.util.UUID;

public class MetricsVerticle extends AbstractVerticle {

    private OperatingSystemMXBean systemMBean;
    private KafkaWriteStream<String, JsonObject> producer;

    @Override
    public void start() throws Exception {
        systemMBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // A random identifier
        String pid = UUID.randomUUID().toString();

        // Get the kafka producer config
        JsonObject config = config();

        // Create the producer
        producer = KafkaWriteStream.create(vertx, config.getMap(), String.class, JsonObject.class);

        // Publish the metircs in Kafka
        vertx.setPeriodic(1000, id -> {
            System.out.println(String.format("Send %d: %d", id, System.currentTimeMillis()));
            producer.write(new ProducerRecord<>("the_topic", new JsonObject().put("id", id)));
        });
    }

    @Override
    public void stop() throws Exception {
        if (producer != null) {
            producer.close();
        }
    }
}
