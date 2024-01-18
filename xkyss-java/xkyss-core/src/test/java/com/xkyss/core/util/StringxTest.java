package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringxTest {

    @Test
    public void test_postSub() {
        Assertions.assertNull(Stringx.postSub(null, ""));
        Assertions.assertNull(Stringx.postSub(null, "00"));
        Assertions.assertEquals("4301", Stringx.postSub("43010000", "00"));
        Assertions.assertEquals("43010", Stringx.postSub("4301000", "00"));
        Assertions.assertEquals("43010", Stringx.postSub("430100000", "00"));
        Assertions.assertEquals("4301", Stringx.postSub("430100000", "0"));
    }

    @Test
    public void test_splitByLength() {
        Assertions.assertArrayEquals(
            new String[]{"aa", "bb", "cc", "dd", "e"}, Stringx.splitByLength("aabbccdde", 2));
        Assertions.assertArrayEquals(
            new String[]{"E", "A", "P"}, Stringx.splitByLength("EAP", 1));
        Assertions.assertArrayEquals(
            new String[]{"Are", "You", "Ok"}, Stringx.splitByLength("AreYouOk", 3));
    }

}
