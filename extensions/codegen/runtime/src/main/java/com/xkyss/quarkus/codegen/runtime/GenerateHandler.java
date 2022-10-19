package com.xkyss.quarkus.codegen.runtime;

import com.xkyss.core.util.Stringx;
import com.xkyss.quarkus.codegen.runtime.model.Column;
import com.xkyss.quarkus.codegen.runtime.model.ColumnType;
import com.xkyss.quarkus.codegen.runtime.model.Table;
import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.runtime.DataSources;
import io.quarkus.devconsole.runtime.spi.DevConsolePostHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.Json;
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
                for (Table t: tables) {
                    log.info(Json.encodePrettily(t));
                }
            }
            catch (Throwable t) {
                context.fail(t);
            }
        }
    }

    private List<Table> generateUseDb(CodegenConfig.SourceConfig source, CodegenConfig.TargetConfig target) throws SQLException {
        AgroalDataSource dataSource = DataSources.fromName(source.dsName());
        Connection connection = dataSource.getConnection();

        // 表信息
        List<Table> tables = new ArrayList<>();
        {
            ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", new String[] {"TABLE", "VIEW"});
            while (rs.next()) {
                Table table = new Table();
                table.setCatalog(rs.getString("TABLE_CAT"));
                table.setSchema(rs.getString("TABLE_SCHEM"));
                table.setName(rs.getString("TABLE_NAME"));
                table.setRemarks(rs.getString("REMARKS"));

                // 保存
                tables.add(table);
            }
        }

        // 字段信息
        for (Table table: tables) {
            ResultSet rs = connection.getMetaData().getColumns(connection.getCatalog(), table.getSchema(), table.getName(), "%");
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                if (Stringx.isNullOrEmpty(name)) {
                    continue;
                }
                Column column = table.getColumn(name);
                if (column == null) {
                    column = new Column();
                    table.addColumn(column);
                }
                column.setName(name);
                column.setNullable(rs.getInt("NULLABLE") > 0);
                column.setRemarks(rs.getString("REMARKS"));
                column.setSize(rs.getInt("COLUMN_SIZE"));
                column.setPrimary(false);
                column.setType(ColumnType.ofSqlType(rs.getInt("DATA_TYPE")));
            }
        }

        // 主键信息
        for (Table table: tables) {
            ResultSet rs = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), table.getSchema(), table.getName());
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                if (Stringx.isNullOrEmpty(name)) {
                    continue;
                }
                Column column = table.getColumn(name);
                if (column == null) {
                    log.errorf("Key %s not in Table %d ???", name, table.getName());
                    continue;
                }
                column.setPrimary(true);
            }
        }

        return tables;
    }
}
