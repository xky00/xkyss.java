package com.xkyss.quarkus.extension.codebe.runtime;

import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

@Recorder
public class CodebeRecorder {
    private static final Logger log = Logger.getLogger(CodebeRecorder.class);

    public Handler<RoutingContext> handler() {
        return new CodebePostHandler();
    }
}
