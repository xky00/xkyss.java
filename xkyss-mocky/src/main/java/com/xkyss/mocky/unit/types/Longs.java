package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Validate;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.mocky.contant.MockConsts.*;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Longs implements MockUnit<Long> {
    private final Random random;

    public Longs(Random random) {
        this.random = random;
    }

    @Override
    public Long get() {
        return random.nextLong();
    }

    public MockUnit<Long> range(long lowerBound, long upperBound) {
        isTrue(lowerBound>=0, LOWER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>0, UPPER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>lowerBound, UPPER_BOUND_BIGGER_LOWER_BOUND);

        return () -> bound(upperBound - lowerBound).get() + lowerBound;
    }

    public MockUnit<Long> bound(Long bound) {
        isTrue(bound > 0, LOWER_BOUND_BIGGER_THAN_ZERO);

        return () -> {
            long b;
            long result;
            do {
                b = (random.nextLong() << 1) >>> 1;
                result = b % bound;
            } while (b-result+bound-1 < 0L);

            return result;
        };
    }

    public MockUnit<Long> from(long[] alphabet) {
        Validate.notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
