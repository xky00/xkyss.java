package com.xkyss.mocky.abstraction;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public abstract class ACachedDictUnit<T, TIn> implements MockUnit<T> {

    protected List<T> cache = null;

    protected final Random random;

    public ACachedDictUnit(Random random) {
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

    protected abstract List<T> read(String path, Function<TIn, T> converter);

    /**
     * 指定字典路径
     */
    protected abstract String path();

    /**
     * 对象转换器
     * @param record csv记录
     * @return 转换后的对象
     */
    protected abstract T convert(TIn record);

}
