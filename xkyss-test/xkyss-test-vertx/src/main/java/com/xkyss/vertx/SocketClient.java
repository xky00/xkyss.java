package com.xkyss.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static java.lang.System.setProperty;

public class SocketClient extends AbstractVerticle {

    public static void main(String[] args) {
        setProperty (LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());

        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new SocketClient());
    }

    private final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    @Override
    public void start() {
        logger.info("starting...");

        NetClientOptions options = new NetClientOptions()
            .setLogActivity(true)
            .setConnectTimeout(10000);
        NetClient client = vertx.createNetClient(options);

        client
            .connect(4321, "localhost")
            .onSuccess(socket -> {
                socket.handler(buffer -> {
                    logger.info("Received data: {}", buffer.toString("UTF-8"));
                });
                socket.write("Hello");
            })
            .onFailure(e -> logger.error("Failed to connect.", e));
            ;

        EventBus eb = vertx.eventBus();
        eb.send("mlcache-demo", "yay! Someone kicked a ball.");
    }
}
