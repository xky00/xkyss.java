package com.xkyss.mocky.base.types;

import com.xkyss.mocky.base.objects.Froms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static com.xkyss.mocky.contant.Alphabets.SMALL_PRIMES;

public class PrimesTest {

    Primes primes;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        primes = new Primes(new Froms(random));
    }

    @Test
    public void testHexa() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(SMALL_PRIMES.contains(primes.get()));
        }
    }
}
