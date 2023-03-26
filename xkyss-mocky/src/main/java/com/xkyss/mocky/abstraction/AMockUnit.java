package com.xkyss.mocky.abstraction;

import java.util.function.Supplier;

public abstract class AMockUnit<T> implements MockUnit<T> {
    final protected Supplier<T> supplier;

    protected AMockUnit(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return supplier.get();
    }
}
