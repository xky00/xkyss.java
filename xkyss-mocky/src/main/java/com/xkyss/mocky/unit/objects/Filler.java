package com.xkyss.mocky.unit.objects;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static org.apache.commons.lang3.Validate.notNull;

public class Filler<T> implements MockUnit<T> {

    private final Supplier<T> supplier;

    @SuppressWarnings("rawtypes")
    private final Map<BiConsumer, MockUnit> setters = new LinkedHashMap<>();

    public Filler(Supplier<T> supplier) {
        notNull(supplier, "supplier is null");
        this.supplier = supplier;
    }

    @Override
    public T get() {
        T o = supplier.get();
        //noinspection unchecked
        setters.forEach((k,v) ->  k.accept(o, v.get()));
        return o;
    }

    public <R> Filler<T> setter(BiConsumer<T, R> setter, MockUnit<R> mockUnit) {
        notNull(setter, "setter");
        notNull(mockUnit, "mockUnit");

        setters.put(setter, mockUnit);
        return this;
    }

    public <R> Filler<T> constant(BiConsumer<T, R> setter, R constant) {
        notNull(setter, "setter");
        notNull(constant, "constant");

        setters.put(setter, () -> constant);
        return this;
    }
}
