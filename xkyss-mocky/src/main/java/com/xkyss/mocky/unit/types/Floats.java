package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Validate;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.core.util.Validate.isFinite;
import static com.xkyss.mocky.contant.MockConsts.*;
import static org.apache.commons.lang3.Validate.*;

public class Floats implements MockUnit<Float> {
    private final Random random;

    public Floats(Random random) {
        this.random = random;
    }

    @Override
    public Float get() {
        return random.nextFloat();
    }

    public MockUnit<Float> range(float lowerBound, float upperBound) {
        isFinite(lowerBound);
        isFinite(upperBound);
        isTrue(lowerBound>=0.0f, LOWER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>0.0f, UPPER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>lowerBound, UPPER_BOUND_BIGGER_LOWER_BOUND);

        return () -> random.nextFloat() * (upperBound - lowerBound) + lowerBound;
    }

    public MockUnit<Float> bound(float bound) {
        return range(0f, bound);
    }

    public MockUnit<Float> from(float[] alphabet) {
        Validate.notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
