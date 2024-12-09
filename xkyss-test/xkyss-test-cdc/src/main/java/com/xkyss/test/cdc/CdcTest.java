package com.xkyss.test.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;


public class CdcTest {
    public static void main(String[] args) throws Exception {
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
            .hostname("localhost")
            .port(3386)
            .databaseList("codex") // set captured database, If you need to synchronize the whole database, Please set tableList to ".*".
            .tableList("codex.t_demo") // set captured table
            .username("root")
            .password("password")
            .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
            .build();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // enable checkpoint
        env.enableCheckpointing(3000);

        env
            .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source")
            // set 1 parallel source tasks
            .setParallelism(1)
            .print().setParallelism(1); // use parallelism 1 for sink

        env.execute("Print MySQL Snapshot + Binlog");
    }
}