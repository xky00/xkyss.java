package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Strings extends MockUnit<String> {

    static Strings defaultOf(Random random) {
        return new StringsImpl(random);
    }

    default MockUnit<String> size(int size) {
        throw new NotImplementedException();
    }

    default MockUnit<String> size(MockUnit<Integer> sizeUnit) {
        throw new NotImplementedException();
    }
}
