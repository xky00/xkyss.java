package com.xkyss.mocky.base.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;

public class BoolsTest {

    Bools bools;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        bools = new Bools(random, new Doubles(random));
    }

    @Test
    public void test100ProbabilityVal() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(bools.probability(100.0).get());
        }
    }

    @Test
    public void testNextBooleanAlwaysFalseIf() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertFalse(bools.probability(0).get());
        }
    }

    @Test
    public void testNextBooleanNegativeNotProbability() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> bools.probability(-5.0).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> bools.probability(105.0).get());
    }

    @Test
    public void testBools() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertNotNull(bools.get());
        }
    }
}
