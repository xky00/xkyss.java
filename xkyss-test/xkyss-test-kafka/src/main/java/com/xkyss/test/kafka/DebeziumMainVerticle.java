package com.xkyss.test.kafka;

import io.debezium.kafka.KafkaCluster;
import io.debezium.util.Testing;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.io.File;
import java.util.Map;

public class DebeziumMainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setWarningExceptionTime(2000);
        options.setMaxWorkerExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setMaxWorkerExecuteTime(2000);
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(new DebeziumMainVerticle());
    }

    private KafkaCluster kafkaCluster;

    @Override
    public void start() throws Exception {

        try {
            // Kafka setup for the example
            File dataDir = Testing.Files.createTestingDirectory("cluster");
            dataDir.deleteOnExit();
            kafkaCluster = new KafkaCluster()
                .usingDirectory(dataDir)
                // .withPorts(2181, 9092)
                .addBrokers(1)
                .deleteDataPriorToStartup(true)
                .startup();

            // Deploy the dashboard
            JsonObject consumerConfig = new JsonObject((Map) kafkaCluster.useTo()
                .getConsumerProperties("the_group", "the_client", OffsetResetStrategy.LATEST));
            System.out.println("Deploy the_client " + OffsetResetStrategy.LATEST);

            vertx.deployVerticle(
                DashboardVerticle.class.getName(),
                new DeploymentOptions().setConfig(consumerConfig)
            );

            // Deploy the metrics collector : 3 times
            for (int i = 0; i < 3; i++) {
                System.out.println("Deploy the_producer-" + i);
                JsonObject producerConfig = new JsonObject((Map) kafkaCluster.useTo()
                    .getProducerProperties("the_producer-" + i));
                vertx.deployVerticle(
                    MetricsVerticle.class.getName(),
                    new DeploymentOptions().setConfig(producerConfig)
                );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        kafkaCluster.shutdown();
    }
}
