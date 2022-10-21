package com.xkyss.quarkus.extension.codegen.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.agroal.api.AgroalDataSource;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@QuarkusTest
public class CodegenResourceTest {

    private static final Logger log = Logger.getLogger(CodegenResourceTest.class);

    @Inject
    AgroalDataSource dataSource;

    @Test
    public void test() {
        log.info("test nothing");
    }

    @Test
    public void testHelloEndpoint() {
        given()
            .when().get("/xkyss-quarkus-extension-codegen")
            .then()
            .statusCode(200)
            .body(is("Hello xkyss-quarkus-extension-codegen"));
    }

    @Test
    public void testDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            return;
        }

        List<Table> table = new ArrayList<>();
        {
            ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", new String[] {"TABLE", "VIEW"});

            log.info("\n\n表信息:");
            while (rs.next()) {
                log.info("-----");
                log.infof("     TABLE_CAT: %s", rs.getString("TABLE_CAT"));
                log.infof("   TABLE_SCHEM: %s", rs.getString("TABLE_SCHEM"));
                log.infof("    TABLE_NAME: %s", rs.getString("TABLE_NAME"));
                log.infof("    TABLE_TYPE: %s", rs.getString("TABLE_TYPE"));
                log.infof("       REMARKS: %s", rs.getString("REMARKS"));
                log.infof("      TYPE_CAT: %s", rs.getString("TYPE_CAT"));
                log.infof("    TYPE_SCHEM: %s", rs.getString("TYPE_SCHEM"));
                log.infof("     TYPE_NAME: %s", rs.getString("TYPE_NAME"));
                log.infof("   SC_COL_NAME: %s", rs.getString("SELF_REFERENCING_COL_NAME"));
                log.infof("REF_GENERATION: %s", rs.getString("REF_GENERATION"));

                Table t = new Table();
                t.schema = rs.getString("TABLE_SCHEM");
                t.name = rs.getString("TABLE_NAME");
                table.add(t);
            }
            rs.close();
        }

        for (Table t: table) {
            log.info("\n\n主键信息:");
            log.infof("Table Primary Key: %s", t.name);
            ResultSet rs = connection.getMetaData().getPrimaryKeys(connection.getCatalog(), t.schema, t.name);
            while (rs.next()) {
                log.info("-----");
                log.infof("     TABLE_CAT: %s", rs.getString("TABLE_CAT"));
                log.infof("   TABLE_SCHEM: %s", rs.getString("TABLE_SCHEM"));
                log.infof("    TABLE_NAME: %s", rs.getString("TABLE_NAME"));
                log.infof("   COLUMN_NAME: %s", rs.getString("COLUMN_NAME"));
                log.infof("       KEY_SEQ: %s", rs.getString("KEY_SEQ"));
                log.infof("       PK_NAME: %s", rs.getString("PK_NAME"));
            }
            rs.close();
        }

        for (Table t: table) {
            log.info("\n\n字段信息:\n");
            log.infof("Table Base Column: %s\n", t.name);
            ResultSet rs = connection.getMetaData().getColumns(connection.getCatalog(), t.schema, t.name, "%");
            while (rs.next()) {
                log.info("-----");
//                log.infof("         TABLE_CAT: %s", rs.getString("TABLE_CAT"));
//                log.infof("       TABLE_SCHEM: %s", rs.getString("TABLE_SCHEM"));
//                log.infof("        TABLE_NAME: %s", rs.getString("TABLE_NAME"));
                log.infof("       COLUMN_NAME: %s", rs.getString("COLUMN_NAME"));
                log.infof("         DATA_TYPE: %s", rs.getString("DATA_TYPE"));
                log.infof("         TYPE_NAME: %s", rs.getString("TYPE_NAME"));
                log.infof("       COLUMN_SIZE: %s", rs.getString("COLUMN_SIZE"));
//                log.infof("     BUFFER_LENGTH: %s", rs.getString("BUFFER_LENGTH"));
//                log.infof("    DECIMAL_DIGITS: %s", rs.getString("DECIMAL_DIGITS"));
//                log.infof("    NUM_PREC_RADIX: %s", rs.getString("NUM_PREC_RADIX"));
                log.infof("          NULLABLE: %s", rs.getString("NULLABLE"));
                log.infof("           REMARKS: %s", rs.getString("REMARKS"));
//                log.infof("        COLUMN_DEF: %s", rs.getString("COLUMN_DEF"));
//                log.infof("     SQL_DATA_TYPE: %s", rs.getString("SQL_DATA_TYPE"));
//                log.infof("  SQL_DATETIME_SUB: %s", rs.getString("SQL_DATETIME_SUB"));
                log.infof(" CHAR_OCTET_LENGTH: %s", rs.getString("CHAR_OCTET_LENGTH"));
                log.infof("  ORDINAL_POSITION: %s", rs.getString("ORDINAL_POSITION"));
//                log.infof("       IS_NULLABLE: %s", rs.getString("IS_NULLABLE"));
//                log.infof("     SCOPE_CATALOG: %s", rs.getString("SCOPE_CATALOG"));
//                log.infof("      SCOPE_SCHEMA: %s", rs.getString("SCOPE_SCHEMA"));
//                log.infof("       SCOPE_TABLE: %s", rs.getString("SCOPE_TABLE"));
//                log.infof("  SOURCE_DATA_TYPE: %s", rs.getString("SOURCE_DATA_TYPE"));
//                log.infof("  IS_AUTOINCREMENT: %s", rs.getString("IS_AUTOINCREMENT"));
//                log.infof("IS_GENERATEDCOLUMN: %s", rs.getString("IS_GENERATEDCOLUMN"));
            }
            rs.close();
        }
    }

    @Test
    public void test_match() {
        Pattern p = Pattern.compile("^[tT]_.*");
        Assertions.assertTrue(p.matcher("t_user").matches());
    }

    class Table {
        public String name;
        public String schema;
    }
}
