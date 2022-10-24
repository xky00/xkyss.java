package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

public class Strings implements MockUnit<String> {

    private final Random random;

    public Strings(Random random) {
        this.random = random;
    }

    @Override
    public String get() {
        return "S";
    }
}
