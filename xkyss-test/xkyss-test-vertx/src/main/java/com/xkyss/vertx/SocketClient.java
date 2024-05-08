package com.xkyss.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;

public class SocketClient extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new SocketClient());
    }

    @Override
    public void start() {
        NetClientOptions options = new NetClientOptions()
            .setLogActivity(true)
            .setConnectTimeout(10000);
        NetClient client = vertx.createNetClient(options);

        client
            .connect(4321, "localhost")
            .onSuccess(socket -> {
                socket.handler(buffer -> {
                    System.out.println("Received data: " + buffer.toString("UTF-8"));
                });
                socket.write("Hello");
            })
            .onFailure(e -> System.out.println("Failed to connect: " + e.getMessage()))
            ;
    }
}
