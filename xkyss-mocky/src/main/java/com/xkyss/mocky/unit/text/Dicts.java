package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Dicts extends MockUnit<String> {
    static Dicts defaultOf(Random random) {
        return new DictsImpl(random);
    }

    default String get(String type) {
        throw new NotImplementedException();
    }
}
