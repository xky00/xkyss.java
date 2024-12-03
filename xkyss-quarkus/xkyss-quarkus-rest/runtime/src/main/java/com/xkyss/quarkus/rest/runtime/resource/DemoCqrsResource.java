package com.xkyss.quarkus.rest.runtime.resource;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import static com.xkyss.quarkus.rest.constant.Constants.ROUTE_PREFIX;


@IfBuildProfile(anyOf = { "dev", "test", "build" })
@Path(ROUTE_PREFIX + "/demo/cqrs")
public class DemoCqrsResource {
    @Inject
    EventBus eb;

    @Context
    HttpServerRequest request;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> get() {
        // 在这里可以访问 @Context 注入的内容
        return eb.<String>request("greetings", "cqrs")
            .onItem().transform(Message::body);
    }


    @ConsumeEvent("greetings")
    public String hello(String name) {
        // 在这里不能访问 @Context 注入的内容
        return "Hello " + name;
    }
}