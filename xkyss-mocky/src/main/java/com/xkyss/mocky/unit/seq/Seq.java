package com.xkyss.mocky.unit.seq;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Iterator;
import java.util.function.Supplier;

import static com.xkyss.mocky.contant.MockConsts.IMPOSSIBLE_TO_SEQ_OVER_EMPTY_COLLECTION;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Seq<T> implements MockUnit<T> {

    private final Iterable<T> iterable;
    private Iterator<T> iterator;

    private boolean cycle;
    private Supplier<T> after;

    public Seq(Iterable<T> iterable) {
        notNull(iterable, "iterable");
        this.iterable = iterable;
        this.iterator = iterable.iterator();
        isTrue(iterator.hasNext(), IMPOSSIBLE_TO_SEQ_OVER_EMPTY_COLLECTION);
    }


    @Override
    public T get() {
        if (iterator.hasNext())
            return iterator.next();
        else
        if (cycle) {
            this.iterator = iterable.iterator();
            return iterator.next();
        }
        else {
            if (after == null) {
                return null;
            }
            return after.get();
        }
    }

    public Seq<T> cycle(boolean value) {
        this.cycle = value;
        return this;
    }

    public Seq<T> after(T after) {
        this.after = () -> after;
        return this;
    }
    public Seq<T> after(MockUnit<T> after) {
        this.after = after;
        return this;
    }
}
