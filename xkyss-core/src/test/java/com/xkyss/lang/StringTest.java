package com.xkyss.lang;

import com.xkyss.core.util.Stringx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.helpers.MessageFormatter;

public class StringTest {

    @Test
    public void test_substring() {
        Assertions.assertEquals("430105", "430105000000".substring(0, 6));
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
        Assertions.assertEquals("Hello 1 a b c",
                Stringx.format("Hello {} {} {} {}", 1, "a", "b", "c"));
        // 数组
        Assertions.assertEquals("Hello 1 a b c",
                Stringx.arrayFormat("Hello {} {} {} {}", new Object[] {1, "a", "b", "c"}));
        Assertions.assertEquals("Hello 1 a b c",
                Stringx.arrayFormat("Hello {} {} {} {}", new String[] {"1", "a", "b", "c"}));
    }
}
