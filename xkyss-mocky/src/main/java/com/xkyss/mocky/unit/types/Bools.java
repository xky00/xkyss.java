package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Bools extends MockUnit<Boolean> {

    static Bools defaultWith(Random random, DoublesImpl doubles) {
        return new BoolsImpl(random, doubles);
    }

    default MockUnit<Boolean> probability(double probability) {
        throw new NotImplementedException();
    }
}
