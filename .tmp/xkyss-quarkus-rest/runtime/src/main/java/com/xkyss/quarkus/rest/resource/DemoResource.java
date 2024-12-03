package com.xkyss.quarkus.rest.resource;

import io.quarkus.arc.profile.IfBuildProfile;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static com.xkyss.quarkus.rest.constant.Constants.ROUTE_PREFIX;


@IfBuildProfile(anyOf = { "dev", "test", "build" })
@Path(ROUTE_PREFIX + "/demo")
public class DemoResource {

    @GET
    @Path("getResponse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResponse() {
        return Response.ok(JsonObject.of(
                "data", Response.class.getName()
            ).getMap())
            .type(MediaType.APPLICATION_JSON)
            .build();
    }

    @GET
    @Path("getUniResponse")
    public Uni<Response> getUniResponse() {
        return Uni.createFrom().item(getResponse());
    }

    @GET
    @Path("getKsResponse")
    public com.xkyss.quarkus.rest.dto.Response<String> getKsResponse() {
        return com.xkyss.quarkus.rest.dto.Response.success(Response.class.getName());
    }

    @GET
    @Path("getUniKsResponse")
    public Uni<com.xkyss.quarkus.rest.dto.Response<String>> getUniKsResponse() {
        return Uni.createFrom().item(com.xkyss.quarkus.rest.dto.Response.success(Response.class.getName()));
    }
}