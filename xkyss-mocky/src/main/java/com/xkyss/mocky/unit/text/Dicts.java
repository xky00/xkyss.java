package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.io.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Dicts implements MockUnit<String>  {

    // 字典资源目录
    private final String dir = "dicts";

    private final String ext = ".txt";

    private final Map<String, List<String>> cache = new HashMap<>();

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
     *  {dir}/{type}{ext}
     *  默认: dicts/type.txt
     */
    public String get(String type) {
        // TODO: 处理相对路径
        String path = Paths.get(type).isAbsolute() ? type : (dir + "/" + type + ext);

        // 优先读取缓存
        if (cache.containsKey(path)) {
            List<String> caches = cache.get(path);
            return caches.get(random.nextInt(caches.size()));
        }

        // 尝试读取外部文件
        List<String> lines = readFileLines(path);
        if (!CollectionUtils.isEmpty(lines)) {
            return lines.get(random.nextInt(lines.size()));
        }

        // 尝试读取资源文件
        lines = readResourceLines(path);
        if (!CollectionUtils.isEmpty(lines)) {
            return lines.get(random.nextInt(lines.size()));
        }

        return "";
    }

    private List<String> readResourceLines(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        Resources.setCharset(defaultCharset());

        try (
            Reader reader = requireNonNull(Resources.getResourceAsReader(path));
            BufferedReader buff = new BufferedReader(reader);
        ) {
            List<String> lines = buff.lines().collect(toList());
            cache.put(path, lines);
            return lines;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private List<String> readFileLines(String path) {
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        Path p = java.nio.file.Paths.get(path);

        try (Stream<String> stream = Files.lines(p)) {
            List<String> lines = stream.collect(toList());
            cache.put(path, lines);
            return lines;
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
