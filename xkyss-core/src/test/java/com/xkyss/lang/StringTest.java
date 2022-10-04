package com.xkyss.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void test_substring() {
        String s = "430105000000";
        String x = s.substring(0, 6);
        Assertions.assertEquals("430105", s.substring(0, 6));
    }
}
