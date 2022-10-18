package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class CodegenRecorder {

    public Handler<RoutingContext> generateHandler() {
        return new GenerateHandler();
    }

    public void setupContainer(CodegenConfig config) {
        CodegenContainerSupplier.initWith(config);
    }
}
