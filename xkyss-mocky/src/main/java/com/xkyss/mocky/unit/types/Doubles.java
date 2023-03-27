package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Doubles extends MockUnit<Double> {

    static Doubles defaultWith(Random random) {
        return new DoublesImpl(random);
    }

    default MockUnit<Double> range(double lowerBound, double upperBound) {
        throw new NotImplementedException();
    }

    default MockUnit<Double> bound(double bound) {
        throw new NotImplementedException();
    }

    default MockUnit<Double> from(double[] alphabet) {
        throw new NotImplementedException();
    }
}
