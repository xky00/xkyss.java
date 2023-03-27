package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.unit.objects.Froms;

import static com.xkyss.mocky.contant.Alphabets.SMALL_PRIMES;

class PrimesImpl implements Primes {

    private final Froms froms;

    public PrimesImpl(Froms froms) {
        this.froms = froms;
    }

    @Override
    public Integer get() {
        return froms.from(SMALL_PRIMES).get();
    }
}
