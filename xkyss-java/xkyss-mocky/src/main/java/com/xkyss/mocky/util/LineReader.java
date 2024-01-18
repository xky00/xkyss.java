package com.xkyss.mocky.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ibatis.io.Resources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class LineReader {

    public static List<String> readResource(String path) {

        Resources.setCharset(defaultCharset());

        try (
            Reader reader = requireNonNull(Resources.getResourceAsReader(path));
            BufferedReader buff = new BufferedReader(reader);
        ) {
            return buff.lines().collect(toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<String> readFile(String path) {
        Path p = java.nio.file.Paths.get(path);
        try (Stream<String> stream = Files.lines(p)) {
            return stream.collect(toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
