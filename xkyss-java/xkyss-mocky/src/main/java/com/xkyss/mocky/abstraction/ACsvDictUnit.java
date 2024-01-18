package com.xkyss.mocky.abstraction;

import com.xkyss.mocky.util.CsvReader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVRecord;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class ACsvDictUnit<T> extends ACachedDictUnit<T, CSVRecord> {
    
    public ACsvDictUnit(Random random) {
        super(random);
    }

    @Override
    protected List<T> read(String path, Function<CSVRecord, T> converter) {
        // 尝试读取外部文件
        List<T> list = CsvReader.readFile(path, converter);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }

        // 尝试读取资源文件
        list = CsvReader.readResource(path, converter);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }

        return Collections.emptyList();
    }
}
