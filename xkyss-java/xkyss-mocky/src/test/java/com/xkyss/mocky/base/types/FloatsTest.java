package com.xkyss.mocky.base.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static org.apache.commons.lang3.ArrayUtils.toObject;

public class FloatsTest {

    Floats floats;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        floats = new Floats(random);
    }

    @Test
    public void testNextFloatInCorrectRange() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Float d = floats.get();
            Assertions.assertTrue(d >= 0 && d < 1.0);
        }
    }

    @Test
    public void testNextFloatInCorrectBound() {
        final float bound = 0.1f;
        for (int i = 0; i < TEST_COUNT; i++) {
            Float d = floats.bound(bound).get();
            Assertions.assertTrue(d >= 0 && d < bound);
        }
    }

    @Test
    public void testNextFloatInCorrectRange_2() {
        final float lower = 0.12f;
        final float upper = Float.MAX_VALUE;
        for (int i = 0; i < TEST_COUNT; i++) {
            Float d = floats.range(lower, upper).get();
            Assertions.assertTrue(d >= lower && d < upper);
        }
    }

    @Test
    public void testNextFloatZeroNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.bound(0.0f).get());
    }
    @Test
    public void testNextFloatNullNotBound() {
        Assertions.assertThrows(NullPointerException.class, () -> floats.bound((Float)null).get());
    }

    @Test
    public void testNextFloatNegativeNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.bound(-1.0f).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.bound(-5.0f).get());
    }

    @Test
    public void testNextFloatBoundsNotEqual() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(0.01f, 0.01f).get());
    }

    @Test
    public void testNextFloatNaNNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(Float.NaN, 10.0f).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(10.0f, Float.NaN).get());
    }

    @Test
    public void testNextFloatInfinityNotBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(Float.POSITIVE_INFINITY, 10.0f).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(10.0f, Float.POSITIVE_INFINITY).get());
    }

    @Test
    public void testNextFloatLesserUpperBound() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(2.0f, 1.0f).get());
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.range(5.0f, 3.0f).get());
    }

    @Test
    public void testNextFloatCorrectValues() {
        float[] values = { 1.0f, 5.0f, 10.0f, 15.0f, 20.52f };
        Set<Float> checks = new HashSet<>(Arrays.asList(toObject(values)));

        for (int i = 0; i < TEST_COUNT; i++) {
            Float d = floats.from(values).get();
            Assertions.assertTrue(checks.contains(d));
        }
    }

    @Test
    public void testNextFloatCorrectValues2() {
        float[] values = { 0.0f };
        for (int i = 0; i < TEST_COUNT; i++) {
            Float d = floats.from(values).get();
            Assertions.assertEquals(0.0f, d);
        }
    }

    @Test
    public void testNextFloatEmptyArrayNotAlphabet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> floats.from(new float[] {}).get());
    }
}
