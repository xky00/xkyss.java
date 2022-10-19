package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberxTest {

    @Test
    public void test_isAll() {
        Assertions.assertTrue(Numberx.isAll(0, 0, 0));
        Assertions.assertTrue(Numberx.isAll(1, 1, 1));
        Assertions.assertTrue(Numberx.isAll(1.0, 1.0, 1.0));

        Assertions.assertTrue(Numberx.isAllZero(0, 0, 0));
        Assertions.assertTrue(Numberx.isAllZero(0f, 0, 0));
        Assertions.assertTrue(Numberx.isAllZero(0, 0L, 0));
        Assertions.assertTrue(Numberx.isAllZero(0, 0, (byte)0));
    }
}
