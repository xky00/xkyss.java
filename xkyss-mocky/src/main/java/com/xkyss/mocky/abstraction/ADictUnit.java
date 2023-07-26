package com.xkyss.mocky.abstraction;

import com.xkyss.mocky.util.CsvReader;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVRecord;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class ADictUnit<T> implements MockUnit<T> {

    protected List<T> cache = null;

    protected final Random random;

    public ADictUnit(Random random) {
        this.random = random;
    }

    @Override
    public T get() {
        if (cache != null) {
            return cache.get(random.nextInt(cache.size()));
        }

        cache = read(path(), this::convert);
        if (!cache.isEmpty()) {
            return cache.get(random.nextInt(cache.size()));
        }

        return null;
    }

    private List<T> read(String path, Function<CSVRecord, T> converter) {
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

    /**
     * 指定字典路径
     */
    protected abstract String path();

    /**
     * 对象转换器
     * @param record csv记录
     * @return 转换后的对象
     */
    protected abstract T convert(CSVRecord record);

}
