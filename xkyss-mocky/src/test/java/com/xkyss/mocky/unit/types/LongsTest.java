package com.xkyss.mocky.unit.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static org.apache.commons.lang3.ArrayUtils.toObject;

public class LongsTest {

    Longs longs;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        longs = new Longs(random);
    }

    @Test
    public void testNextIntegerInCorrectRange() {
        long upperBound = 100;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.bound(upperBound).get();
            Assertions.assertTrue(d >= 0 && d < 100);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange2() {
        long upperBound = 1;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.bound(upperBound).get();
            Assertions.assertTrue(d == 0);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange3() {
        long upperBound = Long.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.bound(upperBound).get();
            Assertions.assertTrue(d < Long.MAX_VALUE);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange4() {
        long lowerBound = 5;
        long upperBound = 8;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d >= lowerBound && d < upperBound);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange5() {
        long lowerBound = 0;
        long upperBound = Integer.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d >= lowerBound && d < upperBound);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange6() {
        long lowerBound = 10;
        long upperBound = lowerBound + 1;
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d == lowerBound);
        }
    }

    @Test
    public void testNextCorrectValues() {
        long[] alphabet = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000};
        Set<Long> checks = new HashSet<>(Arrays.asList(toObject(alphabet)));
        for (int i = 0; i < TEST_COUNT; i++) {
            Long d = longs.from(alphabet).get();
            Assertions.assertTrue(checks.contains(d));
        }
    }

    @Test
    public void testNextIntegerNegativeNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.bound(-100L).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.range(-1L, 100L).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.range(100L, -5L).get());
    }

    @Test
    public void testNextIntegerNonEqualBounds() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.range(1L, 1L).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.range(10L, 10L).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.range(Long.MAX_VALUE, Long.MAX_VALUE).get());
    }

    @Test
    public void testNextIntegerNullNotBound() {
        Assertions.assertThrows(NullPointerException.class, () -> longs.bound(null).get());
        Assertions.assertThrows(NullPointerException.class, () -> longs.range(null, 100L).get());
        Assertions.assertThrows(NullPointerException.class, () -> longs.range(1L, null).get());
    }

    @Test
    public void testNextNulLNotAlphabet() {
        Assertions.assertThrows(NullPointerException.class, () -> longs.from(null).get());
    }

    @Test
    public void testNextEmptyArrayNotAlphabet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> longs.from(new long[]{}).get());
    }
}
