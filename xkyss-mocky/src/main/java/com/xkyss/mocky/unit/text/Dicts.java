package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.io.Resources;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Dicts implements MockUnit<String>  {

    // 字典资源目录
    private final String dir = "dicts";

    private final Random random;

    public Dicts(Random random) {
        this.random = random;
    }

    /**
     * TODO: 随机从现有的字典返回一个
     * @return
     */
    @Override
    public String get() {
        return null;
    }

    /**
     * 子目录:
     *  dicts/type
     * @param type
     * @return
     */
    public String get(String type) {
        String path = dir + "/" + type;

        List<String> lines = readFileLines(path);
        if (CollectionUtils.isEmpty(lines)) {
            lines = readResourceLines(path);
        }

        if (CollectionUtils.isEmpty(lines)) {
            return "";
        }

        int idx = random.nextInt(lines.size());
        return lines.get(idx);
    }

    private List<String> readResourceLines(String path) {
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

    private List<String> readFileLines(String path) {
        Path p = java.nio.file.Paths.get(path);

        try (Stream<String> stream = Files.lines(p)) {
            return stream.collect(toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
