package com.xkyss.mocky.abstraction;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Everything is MockUnit
 * @param <T> 数据类型
 * @author xkyii
 * @date 2022-10-24
 */
public interface MockUnit<T> extends Supplier<T> {

    default <R> MockUnit<R> map(Function<T, R> function) {
        notNull(function, "function");
        return () -> function.apply(get());
    }
}
