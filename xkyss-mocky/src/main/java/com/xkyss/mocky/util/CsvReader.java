package com.xkyss.mocky.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ibatis.io.Resources;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class CsvReader {

    public static <T> List<T> readResource(String path, Function<CSVRecord, T> converter) {

        Resources.setCharset(defaultCharset());

        try (
            Reader reader = requireNonNull(Resources.getResourceAsReader(path));
            CSVParser parser = CSVFormat.DEFAULT.parse(reader)
        ) {
            return parser.stream()
                .map(converter)
                .collect(toList());

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static <T> List<T> readFile(String path, Function<CSVRecord, T> converter) {
        try (Reader reader = new FileReader(path);
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)
        ) {
            return parser.stream()
                .map(converter)
                .collect(toList());

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
