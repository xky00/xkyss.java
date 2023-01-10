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
import static java.util.Arrays.asList;

public class DoublesTest {

    Doubles doubles;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        doubles = new Doubles(random);
    }

    @Test
    public void testNextDoubleInCorrectRange() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Double d = doubles.get();
            Assertions.assertTrue(d >= 0 && d < 1.0);
        }
    }

    @Test
    public void testNextDoubleInCorrectBound() {
        final double bound = 0.1;
        for (int i = 0; i < TEST_COUNT; i++) {
            Double d = doubles.bound(bound).get();
            Assertions.assertTrue(d >= 0 && d < bound);
        }
    }

    @Test
    public void testNextDoubleInCorrectRange_2() {
        final double lower = 0.12;
        final double upper = Double.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Double d = doubles.range(lower, upper).get();
            Assertions.assertTrue(d >= lower && d < upper);
        }
    }

    @Test
    public void testNextDoubleZeroNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.bound(0.0).get());
    }
    @Test
    public void testNextDoubleNullNotBound() {
        Assertions.assertThrows(NullPointerException.class, () -> doubles.bound((Double)null).get());
    }

    @Test
    public void testNextDoubleNegativeNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.bound(-1.0).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.bound(-5.0).get());
    }

    @Test
    public void testNextDoubleBoundsNotEqual() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(0.01, 0.01).get());
    }

    @Test
    public void testNextDoubleNaNNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(Double.NaN, 10.0).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(10.0, Double.NaN).get());
    }

    @Test
    public void testNextDoubleInfinityNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(Double.POSITIVE_INFINITY, 10.0).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(10.0, Double.POSITIVE_INFINITY).get());
    }

    @Test
    public void testNextDoubleLesserUpperBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(2.0, 1.0).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.range(5.0, 3.0).get());
    }

    @Test
    public void testNextDoubleCorrectValues() {
        double[] values = { 1.0, 5.0, 10.0, 15.0, 20.52 };
        Set<Double> checks = new HashSet<>(Arrays.asList(Arrayx.toObject(values)));

        for (int i = 0; i < TEST_COUNT; i++) {
            Double d = doubles.from(values).get();
            Assertions.assertTrue(checks.contains(d));
        }
    }

    @Test
    public void testNextDoubleCorrectValues2() {
        double[] values = { 0.0 };
        for (int i = 0; i < TEST_COUNT; i++) {
            Double d = doubles.from(values).get();
            Assertions.assertEquals(0.0, d);
        }
    }

    @Test
    public void testNextDoubleEmptyArrayNotAlphabet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> doubles.from(new double[] {}).get());
    }
}
