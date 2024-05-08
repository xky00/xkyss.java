package com.xkyss.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class SocketServer extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new SocketServer());
    }

    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions()
            .setLogActivity(true)
            .setHost("localhost")
            .setPort(4321);
        NetServer server = vertx.createNetServer(options);

        server
            .connectHandler(socket -> {
                socket.handler(buffer -> {
                    System.out.println("Received data: " + buffer.toString("UTF-8"));
                });
            })
            .listen();
    }
}
