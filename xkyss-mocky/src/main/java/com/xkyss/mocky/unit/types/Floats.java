package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Floats extends MockUnit<Float> {

    static Floats defaultWith(Random random) {
        return new FloatsImpl(random);
    }

    default MockUnit<Float> range(float lowerBound, float upperBound) {
        throw new NotImplementedException();
    }

    default MockUnit<Float> bound(float bound) {
        throw new NotImplementedException();
    }

    default MockUnit<Float> from(float[] alphabet) {
        throw new NotImplementedException();
    }
}
