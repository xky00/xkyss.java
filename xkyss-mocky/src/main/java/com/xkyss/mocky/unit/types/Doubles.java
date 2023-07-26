package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.core.util.Validate.isFinite;
import static com.xkyss.core.util.Validate.notEmpty;
import static com.xkyss.mocky.contant.MockConsts.*;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

public class Doubles implements MockUnit<Double> {

    private static final double DOUBLE_UNIT = 0x1.0p-53;

    private final Random random;

    public Doubles(Random random) {
        this.random = random;
    }

    @Override
    public Double get() {
        return random.nextDouble();
    }

    public MockUnit<Double> range(double lowerBound, double upperBound) {
        isFinite(lowerBound);
        isFinite(upperBound);
        isTrue(lowerBound>=0.0, LOWER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>0.0, UPPER_BOUND_BIGGER_THAN_ZERO);
        isTrue(upperBound>lowerBound, UPPER_BOUND_BIGGER_LOWER_BOUND);

        return () -> {
            // Algorithm implementation from the Java API
            double result = (random.nextLong() >>> 11) * DOUBLE_UNIT;
            if (lowerBound < upperBound) {
                result = result * (upperBound - lowerBound) + lowerBound;
                if (result >= upperBound)
                    result = longBitsToDouble(doubleToLongBits(upperBound) - 1);
            }
            return result;
        };
    }

    public MockUnit<Double> bound(double bound) {
        return range(0.0, bound);
    }

    public MockUnit<Double> from(double[] alphabet) {
        notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
