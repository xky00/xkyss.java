package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static org.apache.commons.lang3.Validate.inclusiveBetween;

public class Bools implements MockUnit<Boolean> {
    private final Random random;
    private final Doubles doubles;

    public Bools(Random random, Doubles doubles) {
        this.random = random;
        this.doubles = doubles;
    }

    @Override
    public Boolean get() {
        return random.nextBoolean();
    }

    public MockUnit<Boolean> probability(double probability) {
        inclusiveBetween(0.0, 100.0, probability);
        return () -> doubles
            .range(0.0, 100.0)
            .get() < probability;
    }
}
