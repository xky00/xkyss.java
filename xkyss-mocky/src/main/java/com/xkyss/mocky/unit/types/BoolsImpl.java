package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

class BoolsImpl implements Bools {

    private final Random random;
    private final DoublesImpl doubles;

    public BoolsImpl(Random random, DoublesImpl doubles) {
        this.random = random;
        this.doubles = doubles;
    }

    @Override
    public Boolean get() {
        return random.nextBoolean();
    }

    @Override
    public MockUnit<Boolean> probability(double probability) {
        inclusiveBetween(0.0, 100.0, probability);
        return () -> doubles
            .range(0.0, 100.0)
            .get() < probability;
    }
}
