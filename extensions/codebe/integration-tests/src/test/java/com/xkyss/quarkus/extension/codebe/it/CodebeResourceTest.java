package com.xkyss.quarkus.extension.codebe.it;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class CodebeResourceTest {

    private static final Logger log = Logger.getLogger(CodebeResourceTest.class);

    @Inject
    AgroalDataSource dataSource;

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/xkyss-quarkus-extension-codebe")
                .then()
                .statusCode(200)
                .body(is("Hello xkyss-quarkus-extension-codebe"));
    }

    @Test
    public void testDataSource() throws SQLException {
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            return;
        }

        ResultSet rs = connection.getMetaData().getTables(connection.getCatalog(), null, "%", new String[] {"TABLE", "VIEW"});
        while (rs.next()) {
            log.info("\n");
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
        }
    }
}
