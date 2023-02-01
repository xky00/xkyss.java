package com.xkyss.quarkus.demo.command;


import com.xkyss.ibatis.jdbc.ScriptRunner;
import io.agroal.api.AgroalDataSource;
import jakarta.inject.Inject;
import org.apache.ibatis.io.Resources;
import org.jboss.logging.Logger;
import picocli.CommandLine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

@CommandLine.Command(name = "run-sql", description = "Run Sql")
public class RunSqlCommand implements Runnable {

    @Inject
    Logger logger;

    @Inject
    AgroalDataSource dataSource;

    @Override
    public void run() {
        logger.info("Run Sql START.");

        String resource = "D:\\foo.sql";
        Resources.setCharset(StandardCharsets.UTF_8);
        try (
            Connection conn = dataSource.getConnection();
            Reader reader1 = new FileReader(resource);
            Reader reader3 = Resources.getResourceAsReader("foo.sql");
            Reader reader2 = new InputStreamReader(new FileInputStream(resource), StandardCharsets.UTF_8)
        ) {

            ScriptRunner runner = new ScriptRunner(conn);
            runner.setSendFullScript(false);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.runScript(reader3);

        } catch (SQLException | IOException e) {
            logger.error("---", e);
            throw new RuntimeException(e);
        }

        logger.info("Run Sql END.");

    }
}
