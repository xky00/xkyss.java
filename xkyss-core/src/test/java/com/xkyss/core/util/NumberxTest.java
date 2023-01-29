package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberxTest {
    @Test
    public void test_ceil() {
        Assertions.assertEquals(1, Numberx.ceil(1, 1));
        Assertions.assertEquals(1, Numberx.ceil(1, 2));
        Assertions.assertEquals(1, Numberx.ceil(2, 3));
        Assertions.assertEquals(2, Numberx.ceil(3, 2));
        Assertions.assertEquals(2, Numberx.ceil(4, 2));
        Assertions.assertEquals(3, Numberx.ceil(9, 3));
        Assertions.assertEquals(4, Numberx.ceil(10, 3));
    }

    @Test
    public void test_split() {
        Assertions.assertArrayEquals(new int[] { 1, 1 }, Numberx.split(2, 1));
        Assertions.assertArrayEquals(new int[] { 1, 1, 1 }, Numberx.split(3, 1));
        Assertions.assertArrayEquals(new int[] { 2, 1 }, Numberx.split(3, 2));
        Assertions.assertArrayEquals(new int[] { 2, 2 }, Numberx.split(4, 2));
        Assertions.assertArrayEquals(new int[] { 3, 3, 3 }, Numberx.split(9, 3));
        Assertions.assertArrayEquals(new int[] { 3, 3, 3, 1 }, Numberx.split(10, 3));
        Assertions.assertArrayEquals(new int[] { 3, 3, 3, 2 }, Numberx.split(11, 3));
    }
}
