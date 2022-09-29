package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Recorder
public class CodegenRecorder {

    private static final Logger log = Logger.getLogger(CodegenRecorder.class);

    static final List<CodegenContainer> CODEGEN_CONTAINERS = new ArrayList<>(2);

    public void resetContainers() {
        CODEGEN_CONTAINERS.clear();
        log.infof("resetContainers, CODEGEN_CONTAINERS.size(): 0");
    }

    public void addContainer(String id, CodegenConfig config) {
        CODEGEN_CONTAINERS.add(new CodegenContainer(id, config));
        log.infof("addContainer, CODEGEN_CONTAINERS.size(): %d", CODEGEN_CONTAINERS.size());
    }

    public Handler<RoutingContext> handler() {
        return new CodegenPostHandler();
    }
}
