package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Checkx;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.mocky.contant.MockConsts.*;

public class Ints implements MockUnit<Integer> {

    private final Random random;

    public Ints(Random random) {
        this.random = random;
    }

    @Override
    public Integer get() {
        return random.nextInt();
    }

    public MockUnit<Integer> range(Integer lowerBound, Integer upperBound) {
        Checkx.notNull(lowerBound, "lowerBound");
        Checkx.notNull(upperBound, "upperBound");
        Checkx.isTrue(lowerBound>=0, LOWER_BOUND_BIGGER_THAN_ZERO);
        Checkx.isTrue(upperBound>0, UPPER_BOUND_BIGGER_THAN_ZERO);
        Checkx.isTrue(upperBound>lowerBound, UPPER_BOUND_BIGGER_LOWER_BOUND);

        return () -> random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public MockUnit<Integer> bound(Integer bound) {
        Checkx.isTrue(bound > 0, LOWER_BOUND_BIGGER_THAN_ZERO);

        return () -> random.nextInt(bound);
    }

    public MockUnit<Integer> from(int[] alphabet) {
        Checkx.notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
