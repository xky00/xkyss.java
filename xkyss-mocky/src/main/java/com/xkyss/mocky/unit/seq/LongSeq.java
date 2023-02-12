package com.xkyss.mocky.unit.seq;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.concurrent.atomic.AtomicLong;

import static com.xkyss.mocky.contant.MockConsts.SEQ_INVALID_RANGE;
import static com.xkyss.mocky.contant.MockConsts.SEQ_OVERFLOW;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static org.apache.commons.lang3.Validate.isTrue;

public class LongSeq implements MockUnit<Long> {

    private long increment;
    private long start; // incremented with 0 by default
    private boolean cycle;
    private long max;
    private long min;
    private AtomicLong internal;


    public LongSeq(long start, long increment, long max, long min, boolean cycle) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.increment = increment;
        this.start = start;
        this.cycle = cycle;
        this.max = max;
        this.min = min;
        this.internal = new AtomicLong(start);
    }

    public LongSeq(long start, long increment) {
        this(start, increment, MAX_VALUE, MIN_VALUE, true);
    }

    public LongSeq(long increment) {
        this(0, increment, MAX_VALUE, MIN_VALUE, true);
    }

    public LongSeq() {
        this(0, 1, MAX_VALUE, MIN_VALUE, true);
    }

    @Override
    public Long get() {
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

    public LongSeq start(long start) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.start = start;
        this.internal = new AtomicLong(start);
        return this;
    }

    public LongSeq increment(long increment) {
        this.increment = increment;
        return this;
    }

    public LongSeq cycle(boolean cycle) {
        this.cycle = cycle;
        return this;
    }

    public LongSeq max(long max) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.max = max;
        return this;
    }

    public LongSeq min(long min) {
        isTrue(min<max, SEQ_INVALID_RANGE, min, max);
        this.min = min;
        return this;
    }

    private boolean nextValueOverflows() {
        return (increment > 0) ? (internal.get() > max) : (internal.get() < min);
    }
}
