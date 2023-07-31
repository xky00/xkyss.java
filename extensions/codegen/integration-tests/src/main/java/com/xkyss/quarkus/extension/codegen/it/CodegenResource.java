package com.xkyss.quarkus.extension.codegen.it;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/xkyss-quarkus-extension-codegen")
@ApplicationScoped
public class CodegenResource {
    // add some rest methods here

    @GET
    public String hello() {
        return "Hello xkyss-quarkus-extension-codegen";
    }
}
