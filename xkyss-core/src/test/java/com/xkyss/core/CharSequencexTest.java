package com.xkyss.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharSequencexTest {

    @Test
    public void test_isNullOrEmpty() {
        Assertions.assertTrue(CharSequencex.isNullOrEmpty(null));
        Assertions.assertTrue(CharSequencex.isNullOrEmpty(""));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty(" "));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty("\t"));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty("\n"));
    }

    @Test
    public void test_isEmpty() {
        Assertions.assertTrue(CharSequencex.isNullOrEmpty(null));
        Assertions.assertTrue(CharSequencex.isNullOrEmpty(""));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty(" "));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty("\t"));
        Assertions.assertFalse(CharSequencex.isNullOrEmpty("\n"));
    }
}
