package com.xkyss.mocky.unit.text;

import com.xkyss.core.util.Checkx;
import com.xkyss.core.util.RandomStringx;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.mocky.contant.MockConst.INPUT_PARAMETER_NOT_NULL;

public class Strings implements MockUnit<String> {

    private final Random random;
    private int size = 64;

    private MockUnit<Integer> mockSize;

    public Strings(Random random) {
        this.random = random;
    }

    @Override
    public String get() {
        return RandomStringx.random(getSize(), 0, 0, true, true, null, random);
    }

    public Strings size(int size) {
        Checkx.isTrue(size>0, "The size needs to be bigger than 0 (>).");
        this.size = size;
        return this;
    }

    public Strings size(MockUnit<Integer> sizeUnit) {
        Checkx.notNull(sizeUnit, INPUT_PARAMETER_NOT_NULL, "sizeUnit");
        this.mockSize = sizeUnit;
        return this;
    }

    protected int getSize() {
        return mockSize != null ? mockSize.get() : size;
    }
}
