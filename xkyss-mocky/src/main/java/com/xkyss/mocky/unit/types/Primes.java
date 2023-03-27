package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.unit.objects.Froms;

public interface Primes extends MockUnit<Integer> {
    static Primes defaultWith(Froms froms) {
        return new PrimesImpl(froms);
    }
}
