package com.xkyss.lang;

import com.xkyss.core.util.Stringx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.MessageFormatter;

public class StringTest {

    @Test
    public void test_substring() {
        String s = "430105000000";
        String x = s.substring(0, 6);
        Assertions.assertEquals("430105", s.substring(0, 6));
    }

    @Test
    public void test_format() {
        // 无参数
        Assertions.assertEquals("Hello {}", Stringx.format("Hello {}"));
        // null
        Assertions.assertEquals("Hello null", Stringx.format("Hello {}", null));
        // 一个参数
        Assertions.assertEquals("Hello 1", Stringx.format("Hello {}", 1));
        // 二个参数
        Assertions.assertEquals("Hello 1 a", Stringx.format("Hello {} {}", 1, "a"));
        // 多个参数
        Assertions.assertEquals("Hello 1 a b c", Stringx.format("Hello {} {} {} {}", 1, "a", "b", "c"));
    }
}
