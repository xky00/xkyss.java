package com.xkyss.mocky.unit.types;

import com.xkyss.core.util.Arrayx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;

public class IntsTest {

    Ints ints;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        ints = new Ints(random);
    }

    @Test
    public void testNextIntegerInCorrectRange() {
        int upperBound = 100;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.bound(upperBound).get();
            Assertions.assertTrue(d >= 0 && d < 100);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange2() {
        int upperBound = 1;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.bound(upperBound).get();
            Assertions.assertTrue(d == 0);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange3() {
        int upperBound = Integer.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.bound(upperBound).get();
            Assertions.assertTrue(d < Integer.MAX_VALUE);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange4() {
        int lowerBound = 5;
        int upperBound = 8;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d >= lowerBound && d < upperBound);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange5() {
        int lowerBound = 0;
        int upperBound = Integer.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d >= lowerBound && d < upperBound);
        }
    }

    @Test
    public void testNextIntegerInCorrectRange6() {
        int lowerBound = 10;
        int upperBound = lowerBound + 1;
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.range(lowerBound, upperBound).get();
            Assertions.assertTrue(d == lowerBound);
        }
    }

    @Test
    public void testNextCorrectValues() {
        int[] alphabet = {10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000};
        Set<Integer> checks = new HashSet<>(Arrays.asList(Arrayx.toObject(alphabet)));
        for (int i = 0; i < TEST_COUNT; i++) {
            Integer d = ints.from(alphabet).get();
            Assertions.assertTrue(checks.contains(d));
        }
    }

    @Test
    public void testNextIntegerNegativeNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.bound(-100).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.range(-1, 100).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.range(100, -5).get());
    }

    @Test
    public void testNextIntegerNonEqualBounds() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.range(1, 1).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.range(10, 10).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.range(Integer.MAX_VALUE, Integer.MAX_VALUE).get());
    }

    @Test
    public void testNextIntegerNullNotBound() {
        Assertions.assertThrows(NullPointerException.class, () -> ints.bound(null).get());
        Assertions.assertThrows(NullPointerException.class, () -> ints.range(null, 100).get());
        Assertions.assertThrows(NullPointerException.class, () -> ints.range(1, null).get());
    }

    @Test
    public void testNextNulLNotAlphabet() {
        Assertions.assertThrows(NullPointerException.class, () -> ints.from(null).get());
    }

    @Test
    public void testNextEmptyArrayNotAlphabet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ints.from(new int[]{}).get());
    }
}
