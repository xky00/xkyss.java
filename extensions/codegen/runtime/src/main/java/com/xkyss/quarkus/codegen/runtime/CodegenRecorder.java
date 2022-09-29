package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

@Recorder
public class CodegenRecorder {

    static final List<CodegenContainer> CODEGEN_CONTAINERS = new ArrayList<>(2);

    public void resetContainers() {
        CODEGEN_CONTAINERS.clear();
    }

    public void addContainer(String id, CodegenConfig config) {
        CODEGEN_CONTAINERS.add(new CodegenContainer(id, config));
    }

    public Handler<RoutingContext> handler() {
        return new CodegenPostHandler();
    }
}
