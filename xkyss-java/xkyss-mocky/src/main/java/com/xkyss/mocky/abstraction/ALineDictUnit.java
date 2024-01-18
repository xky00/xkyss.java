package com.xkyss.mocky.abstraction;

import com.xkyss.mocky.util.LineReader;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class ALineDictUnit extends ACachedDictUnit<String, String> {
    public ALineDictUnit(Random random) {
        super(random);
    }

    @Override
    protected List<String> read(String path, Function<String, String> converter) {
        // 尝试读取外部文件
        List<String> list = LineReader.readFile(path);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }

        // 尝试读取资源文件
        list = LineReader.readResource(path);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }

        return Collections.emptyList();
    }

    @Override
    protected String convert(String s) {
        return null;
    }
}
