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
import java.util.ArrayList;
import java.util.List;

public class GenerateHandler extends DevConsolePostHandler {

    private static final Logger log = Logger.getLogger(GenerateHandler.class);

    @Override
    protected void handlePost(RoutingContext context, MultiMap form) {
        String sourceId = form.get("sourceId");
        String targetId = form.get("targetId");
        log.infof("handlePost, sourceId: %s, targetId: %s", sourceId, targetId);

        CodegenConfig.SourceConfig source = CodegenContainerSupplier.INSTANCE.sources.getOrDefault(sourceId, null);
        if (source == null) {
            log.info("Source is NULL.");
            return;
        }

        CodegenConfig.TargetConfig target = CodegenContainerSupplier.INSTANCE.targets.getOrDefault(targetId, null);
        if (target == null) {
            log.info("Target is NULL.");
            return;
        }

        if (source.kind().equalsIgnoreCase("ENTITY")) {

        }
        else if (source.kind().equalsIgnoreCase("DB")) {
            try {
                List<Table> tables = generateUseDb(source, target);
            }
            catch (Throwable t) {
                context.fail(t);
            }
        }
    }

    private List<Table> generateUseDb(CodegenConfig.SourceConfig source, CodegenConfig.TargetConfig target) throws SQLException {
        AgroalDataSource dataSource = DataSources.fromName(source.dsName());
        Connection connection = dataSource.getConnection();

        ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", new String[] {"TABLE", "VIEW"});

        List<Table> tables = new ArrayList<>();
        while (rs.next()) {
            Table table = new Table();
            table.setCategory(rs.getString("TABLE_CAT"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setRemarks(rs.getString("REMARKS"));
            tables.add(table);
            log.info(table);
        }

        return tables;
    }
}
