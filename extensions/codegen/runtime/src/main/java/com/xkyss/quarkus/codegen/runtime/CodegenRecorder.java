package com.xkyss.quarkus.codegen.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;


@Recorder
public class CodegenRecorder {
    static final List<SourceContainer> SOURCE_CONTAINERS = new ArrayList<>(2);
    static final List<TargetContainer> TARGET_CONTAINERS = new ArrayList<>(2);

    public void resetContainers() {
        SOURCE_CONTAINERS.clear();
        TARGET_CONTAINERS.clear();
    }

    public void addSource(String id, SourceConfig config) {
        SOURCE_CONTAINERS.add(new SourceContainer(id, config));
    }

    public void addTarget(String id, TargetConfig config) {
        TARGET_CONTAINERS.add(new TargetContainer(id, config));

    }

    public Handler<RoutingContext> handler() {
        return new CodegenPostHandler();
    }

}
