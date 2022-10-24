package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Checkx;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

public class Ints implements MockUnit<Integer> {

    private final Random random;

    public Ints(Random random) {
        this.random = random;
    }

    @Override
    public Integer get() {
        return random.nextInt();
    }

    public MockUnit<Integer> from(int[] alphabet) {
        Checkx.notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }
}
