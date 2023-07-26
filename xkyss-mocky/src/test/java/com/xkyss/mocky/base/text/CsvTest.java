package com.xkyss.mocky.base.text;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;

public class CsvTest {

    @Test
    public void test_01() {

        Resources.setCharset(defaultCharset());

        try (
            Reader reader = requireNonNull(Resources.getResourceAsReader("dicts/test.csv"));
            CSVParser parser = CSVFormat.DEFAULT.parse(reader)
        ) {
            List<CSVRecord> records = parser.getRecords();
            Assertions.assertEquals(3, records.size());

            Assertions.assertEquals("aaa", records.get(0).get(0));
            Assertions.assertEquals("111", records.get(0).get(1));
            Assertions.assertEquals("bbb", records.get(1).get(0));
            Assertions.assertEquals("222", records.get(1).get(1));
            Assertions.assertEquals("ccc", records.get(2).get(0));
            Assertions.assertEquals("333", records.get(2).get(1));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
