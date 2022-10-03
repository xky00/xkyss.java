package com.xkyss.core;

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
}
