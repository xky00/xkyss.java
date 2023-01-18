package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

public class Chars implements MockUnit<Character> {

    private final Random random;

    public Chars(Random random) {
        this.random = random;
    }

    @Override
    public Character get() {
        return null;
    }
}
