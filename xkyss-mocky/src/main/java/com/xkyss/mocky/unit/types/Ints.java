package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Validate;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.mocky.contant.MockConsts.*;
import static org.apache.commons.lang3.Validate.*;

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
        notNull(lowerBound, "lowerBound");
        notNull(upperBound, "upperBound");
        isTrue(lowerBound>=0, LOWER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>0, UPPER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>lowerBound, UPPER_BOUND_BIGGER_LOWER_BOUND);

        return () -> random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public MockUnit<Integer> bound(Integer bound) {
        isTrue(bound > 0, LOWER_BOUND_BIGGER_THAN_ZERO);

        return () -> random.nextInt(bound);
    }

    public MockUnit<Integer> from(int[] alphabet) {
        Validate.notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
