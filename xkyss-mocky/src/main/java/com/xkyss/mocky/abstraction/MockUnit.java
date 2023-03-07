package com.xkyss.mocky.abstraction;

import java.util.*;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.xkyss.mocky.contant.MockConsts.SIZE_BIGGER_THAN_ZERO;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.Validate.isTrue;
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

    default List<T> list(int size) {
        return list(ArrayList::new, size);
    }

    default List<T> list(MockUnit<Integer> sizeUnit) {
        notNull(sizeUnit, "sizeUnit");
        return list(sizeUnit.get());
    }

    default List<T> list(Supplier<List<T>> listSupplier, int size) {
        notNull(listSupplier, "listSupplier");
        isTrue(size>=0, SIZE_BIGGER_THAN_ZERO);

        final List<T> result = listSupplier.get();
        notNull(result, "listSupplier");
        range(0, size).forEach(i -> result.add(get()));
        return result;
    }
}
