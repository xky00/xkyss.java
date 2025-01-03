package com.xkyss.test.grpc;

import io.grpc.examples.helloworld.HelloReply;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.grpc.server.GrpcServer;

public class HelloServer extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new HelloServer());
    }

    private final int port;

    public HelloServer(int port) {
        this.port = port;
    }

    public HelloServer() {
        this(8080);
    }

    @Override
    public void start() {

        // Create the server
        GrpcServer rpcServer = GrpcServer.server(vertx);

        // The rpc service
        rpcServer.callHandler(io.grpc.examples.helloworld.GreeterGrpc.getSayHelloMethod(), request -> {
            request
                .last()
                .onSuccess(msg -> {
                    System.out.println("Hello " + msg.getName());
                    request.response().end(HelloReply.newBuilder().setMessage(msg.getName()).build());
                });
        });


        // start the server
        vertx.createHttpServer().requestHandler(rpcServer).listen(8080)
            .onFailure(cause -> {
                cause.printStackTrace();
            });
    }
}