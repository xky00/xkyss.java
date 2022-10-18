package com.xkyss.quarkus.codegen.runtime.handler;

import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

public class GenerateHandler extends DevConsolePostHandler {

    private static final Logger log = Logger.getLogger(GenerateHandler.class);

    @Override
    protected void handlePost(RoutingContext context, MultiMap form) {
        log.info("handlePost");
    }
}
