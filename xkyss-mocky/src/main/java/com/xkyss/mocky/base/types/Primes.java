package com.xkyss.mocky.base.types;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.base.objects.Froms;

import static com.xkyss.mocky.contant.Alphabets.SMALL_PRIMES;

class Primes implements MockUnit<Integer>{

    private final Froms froms;

    public Primes(Froms froms) {
        this.froms = froms;
    }

    @Override
    public Integer get() {
        return froms.from(SMALL_PRIMES).get();
    }
}
