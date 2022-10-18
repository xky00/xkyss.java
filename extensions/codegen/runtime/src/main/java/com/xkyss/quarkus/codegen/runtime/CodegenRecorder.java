package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.quarkus.codegen.runtime.handler.GenerateHandler;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

@Recorder
public class CodegenRecorder {

    public Handler<RoutingContext> generateHandler() {
        return new GenerateHandler();
    }
}
