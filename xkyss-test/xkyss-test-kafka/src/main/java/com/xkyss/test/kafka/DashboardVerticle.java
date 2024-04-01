package com.xkyss.test.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.kafka.client.consumer.KafkaConsumerRecord;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;

public class DashboardVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        // Get the Kafka consumer config
        JsonObject config = config();

        // Create the consumer
        KafkaReadStream<String, JsonObject> consumer = KafkaReadStream.create(vertx, config.getMap(), String.class, JsonObject.class);

        // // Aggregates metrics in the dashboard
        // consumer.handler(record -> {
        //     JsonObject obj = record.value();
        //     System.out.println(String.format("Receive %d: %d", obj.getLong("id"), System.currentTimeMillis()));
        // });

        // consumer.assign(Collections.singleton(new TopicPartition("the_topic", 0)))
        //     .onSuccess(v -> System.out.println("assign to the the_topic partition 0"))
        //     .onFailure(Throwable::printStackTrace);


        // // 将消费者定位到分区的开头
        // TopicPartition topicPartition = new TopicPartition("the_topic", 0);
        // consumer
        //      .seekToBeginning(Collections.singleton(topicPartition))
        //     .onSuccess(v -> System.out.println("Seeked to the beginning"))
        //     .onFailure(Throwable::printStackTrace);

        // seek to a specific offset
        // TopicPartition topicPartition = new TopicPartition("the_topic", 0);
        // consumer
        //     .seek(topicPartition, 10)
        //     .onSuccess(v -> System.out.println("Seeked to the beginning"))
        //     .onFailure(Throwable::printStackTrace);

        consumer.subscribe(Collections.singleton("the_topic"))
            .onSuccess(v -> {
                // 一段时间后,拉一次数据
                vertx.setPeriodic(3000, t -> {
                    consumer.poll(Duration.ofMillis(5000))
                        .onSuccess(records -> {
                            System.out.println("Polled " + records.count() + " records");
                            for (ConsumerRecord<String, JsonObject> record : records) {
                                System.out.println("key=" + record.key() + ",value=" + record.value() +
                                    ",partition=" + record.partition() + ",offset=" + record.offset());
                            }
                            // 从头开始拉取
                            if (!records.isEmpty()) {
                                // seek to a specific offset
                                TopicPartition topicPartition = new TopicPartition("the_topic", 0);
                                consumer
                                    .seekToBeginning(Collections.singleton(topicPartition))
                                    .onSuccess(v1 -> System.out.println("Seeked to the beginning"))
                                    .onFailure(Throwable::printStackTrace);
                            }
                        })
                        .onFailure(Throwable::printStackTrace);
                });
            })
            .onFailure(Throwable::printStackTrace);

    }

    @Override
    public void stop() throws Exception {

    }
}
