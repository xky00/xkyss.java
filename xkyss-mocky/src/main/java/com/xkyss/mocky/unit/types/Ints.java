package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Ints extends MockUnit<Integer> {

    static Ints defaultWith(Random random) {
        return new IntsImpl(random);
    }

    default MockUnit<Integer> range(Integer lowerBound, Integer upperBound) {
        throw new NotImplementedException();
    }

    default MockUnit<Integer> bound(Integer bound) {
        throw new NotImplementedException();
    }

    default MockUnit<Integer> from(int[] alphabet) {
        throw new NotImplementedException();
    }
}
