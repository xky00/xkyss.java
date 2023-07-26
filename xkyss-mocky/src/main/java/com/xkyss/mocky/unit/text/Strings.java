package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;

import java.util.Random;

import static com.xkyss.mocky.contant.MockConsts.INPUT_PARAMETER_NOT_NULL;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

class Strings implements MockUnit<String> {

    private final Random random;

    public Strings(Random random) {
        this.random = random;
    }

    @Override
    public String get() {
        return random(64, 0, 0, true, true, null, random);
    }

    public MockUnit<String> size(int size) {
        isTrue(size>0, "The size needs to be bigger than 0 (>).");
        return () -> random(size, 0, 0, true, true, null, random);
    }

    public MockUnit<String> size(MockUnit<Integer> sizeUnit) {
        notNull(sizeUnit, INPUT_PARAMETER_NOT_NULL, "sizeUnit");
        return () -> random(sizeUnit.get(), 0, 0, true, true, null, random);
    }
}
