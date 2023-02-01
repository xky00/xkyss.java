package com.xkyss.core.jdbc;

import com.xkyss.ibatis.io.Resources;
import com.xkyss.ibatis.jdbc.ScriptRunner;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@QuarkusTest
public class ScriptRunnerTest {

    @Inject
    DataSource dataSource;

    @Test
    public void test_01() {
        Assertions.assertTrue(true);
        Resources.setCharset(StandardCharsets.UTF_8);
    }

    @Test
    public void test_02() {
        String resource = "D:\\foo.sql";
        Resources.setCharset(StandardCharsets.UTF_8);
        try (
            Connection conn = dataSource.getConnection();
//            Reader reader1 = new FileReader(resource);
            Reader reader3 = Resources.getResourceAsReader("foo.sql")
//            Reader reader2 = new InputStreamReader(new FileInputStream(resource), StandardCharsets.UTF_8)
        ) {

            ScriptRunner runner = new ScriptRunner(conn);
            runner.setSendFullScript(false);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.runScript(reader3);

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
