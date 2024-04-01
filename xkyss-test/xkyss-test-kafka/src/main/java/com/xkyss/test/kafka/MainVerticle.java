package com.xkyss.test.kafka;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

public class MainVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setWarningExceptionTime(2000);
        options.setMaxWorkerExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setMaxWorkerExecuteTime(2000);
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(new MainVerticle());
    }


    @Override
    public void start() throws Exception {
        // Deploy the dashboard
        {
            JsonObject consumerConfig = JsonObject.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest",
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false",
                ConsumerConfig.GROUP_ID_CONFIG, "th_group",
                ConsumerConfig.CLIENT_ID_CONFIG, "th_client"
            );
            System.out.println("Deploy the_client " + OffsetResetStrategy.LATEST);

            vertx.deployVerticle(
                DashboardVerticle.class.getName(),
                new DeploymentOptions().setConfig(consumerConfig)
            );
        }

        // Deploy the metrics collector : 3 times
        {
            for (int i = 0; i < 1; i++) {
                System.out.println("Deploy the_producer-" + i);
                JsonObject producerConfig = JsonObject.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                    ProducerConfig.ACKS_CONFIG, "1",
                    ProducerConfig.CLIENT_ID_CONFIG, "the_producer-"+i
                );
                vertx.deployVerticle(
                    MetricsVerticle.class.getName(),
                    new DeploymentOptions().setConfig(producerConfig)
                );
            }
        }
    }

    @Override
    public void stop() throws Exception {
    }
}
