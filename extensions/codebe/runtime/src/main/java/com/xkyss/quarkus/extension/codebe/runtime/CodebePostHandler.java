package com.xkyss.quarkus.extension.codebe.runtime;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

public class CodebePostHandler extends DevConsolePostHandler {
    private static final Logger log = Logger.getLogger(CodebePostHandler.class);

    protected void handlePost(RoutingContext context, MultiMap form) {
        String source = form.get("source");
        log.infof("source: %s", source);

        try (AgroalDataSource ds = DataSources.fromName(source)) {

            flashMessage(context, "Codebe Generate ok");
        }
    }
}
