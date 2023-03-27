package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Longs extends MockUnit<Long> {

    static Longs defaultWith(Random random) {
        return new LongsImpl(random);
    }

    default MockUnit<Long> range(Long lowerBound, Long upperBound) {
        throw new NotImplementedException();
    }

    default MockUnit<Long> bound(Long bound) {
        throw new NotImplementedException();
    }

    default MockUnit<Long> from(long[] alphabet) {
        throw new NotImplementedException();
    }
}
