package com.xkyss.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.parsetools.JsonEventType;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static java.lang.System.setProperty;

public class SocketServer extends AbstractVerticle {

    public static void main(String[] args) {
        setProperty (LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());

        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new SocketServer());
    }

    private final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions()
            .setLogActivity(true)
            .setHost("localhost")
            .setPort(4321);
        NetServer server = vertx.createNetServer(options);

        server
            .connectHandler(socket -> {
                // 换行粘包处理
                // RecordParser.newDelimited("\n", socket)
                //     .endHandler(v -> socket.close())
                //     .exceptionHandler(e -> {
                //         logger.error("Error", e);
                //         socket.close();
                //     })
                //     .handler(buffer -> {
                //         String data = buffer.toString("UTF-8");
                //         logger.info("Received data: {}", data);
                //         // socket.write("Hello " + name + "\n", "UTF-8");
                //     });

                // Json粘包处理
                JsonParser parser = JsonParser.newParser(socket);
                parser.objectValueMode()
                    .endHandler(v -> socket.close())
                    .exceptionHandler(e -> {
                        logger.error("Error", e);
                        socket.close();
                    })
                    .handler(event -> {
                        Message message = event.mapTo(Message.class);
                        logger.info("Received message: {}", message.toString());

                    });
            })
            .listen();

        EventBus eb = vertx.eventBus();
        eb.consumer("mlcache-demo")
            .handler(message -> {
                logger.info("Received message: {}", message.body());
            })
            .completionHandler(res -> {
                if (res.succeeded()) {
                    logger.info("Subscribed to mlcache-demo");
                } else {
                    logger.error("Failed to subscribe to mlcache-demo", res.cause());
                }
            });
    }

    static class Message {
        private String data;
        private String index;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        @Override
        public String toString() {
            return "Message{" +
                "data='" + data + '\'' +
                ", index='" + index + '\'' +
                '}';
        }
    }
}
