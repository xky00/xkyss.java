package com.xkyss.quarkus.codegen.runtime;


import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePostHandler extends DevConsolePostHandler {
    private static final Logger log = Logger.getLogger(DatabasePostHandler.class);

    protected void handlePost(RoutingContext context, MultiMap form) throws SQLException {
        String source = form.get("source");
        log.infof("source: %s", source);

        try (AgroalDataSource ds = DataSources.fromName(source)) {
            Connection connection = ds.getConnection();
            if (connection == null) {
                log.infof("getConnection FAILED: %s", source);
                return;
            }

            ResultSet rs = connection.getMetaData().getTables(connection.getSchema(), null, "%", null);
            while (rs.next()) {
                String tableName = rs.getString(3);
            }
        }

        flashMessage(context, "Codebe Generate ok");
    }
}
