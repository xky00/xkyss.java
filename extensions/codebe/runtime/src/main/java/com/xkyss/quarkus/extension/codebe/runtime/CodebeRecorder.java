package com.xkyss.quarkus.extension.codebe.runtime;

import io.quarkus.datasource.runtime.DatabaseSchemaProvider;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.quarkus.runtime.annotations.Recorder;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import java.util.ServiceLoader;

@Recorder
public class CodebeRecorder {
    private static final Logger log = Logger.getLogger(CodebeRecorder.class);

    public Handler<RoutingContext> test() {
        final ClassLoader currentCl = Thread.currentThread().getContextClassLoader();
        return new DevConsolePostHandler() {

            @Override
            protected void handlePostAsync(RoutingContext event, MultiMap form) throws Exception {
                String name = form.get("name");
                ServiceLoader<DatabaseSchemaProvider> dbs = ServiceLoader.load(DatabaseSchemaProvider.class,
                    Thread.currentThread().getContextClassLoader());
                for (DatabaseSchemaProvider i : dbs) {
                    i.resetDatabase(name);
                }
                flashMessage(event, "Database successfully reset");
            }
        };
    }
}
