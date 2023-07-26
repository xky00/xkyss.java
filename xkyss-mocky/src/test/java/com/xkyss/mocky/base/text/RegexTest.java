package com.xkyss.mocky.base.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;

public class RegexTest {

    @Test
    public void testNulLRegex() {
        Assertions.assertThrows(NullPointerException.class, () -> new Regex(null).get());
    }

    @Test
    public void testInvalidRegex() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Regex("1{").get());
    }

    @Test
    public void testFromRegex() {
        final String p = "[0-3]([a-c]|[e-g]{1,2})";
        for (int i = 0; i < TEST_COUNT; i++) {
            String s = new Regex(p).get();
            Assertions.assertTrue(s.matches(p));
        }
    }
}
