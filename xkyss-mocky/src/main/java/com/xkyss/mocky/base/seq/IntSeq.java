package com.xkyss.mocky.base.seq;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.concurrent.atomic.AtomicInteger;

import static com.xkyss.mocky.contant.MockConsts.SEQ_INVALID_RANGE;
import static com.xkyss.mocky.contant.MockConsts.SEQ_OVERFLOW;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.apache.commons.lang3.Validate.isTrue;

public class IntSeq implements MockUnit<Integer> {

    private int increment;
    private int start; // incremented with 0 by default
    private boolean cycle;
    private int max;
    private int min;
    private AtomicInteger internal;


    public IntSeq(int start, int increment, int max, int min, boolean cycle) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.increment = increment;
        this.start = start;
        this.cycle = cycle;
        this.max = max;
        this.min = min;
        this.internal = new AtomicInteger(start);
    }

    public IntSeq(int start, int increment) {
        this(start, increment, MAX_VALUE, MIN_VALUE, true);
    }

    public IntSeq(int increment) {
        this(0, increment, MAX_VALUE, MIN_VALUE, true);
    }

    public IntSeq() {
        this(0, 1, MAX_VALUE, MIN_VALUE, true);
    }

    @Override
    public Integer get() {
        if (nextValueOverflows()) {
            if (cycle) {
                internal.set(start);
            }
            else {
                //noinspection DataFlowIssue
                isTrue(false, SEQ_OVERFLOW, min, max);
            }
        }
        return internal.getAndAdd(increment);
    }

    public IntSeq start(int start) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.start = start;
        this.internal = new AtomicInteger(start);
        return this;
    }

    public IntSeq increment(int increment) {
        this.increment = increment;
        return this;
    }

    public IntSeq cycle(boolean cycle) {
        this.cycle = cycle;
        return this;
    }

    public IntSeq max(int max) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.max = max;
        return this;
    }

    public IntSeq min(int min) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.min = min;
        return this;
    }

    private boolean nextValueOverflows() {
        return (increment > 0) ? (internal.get() > max) : (internal.get() < min);
    }
}
