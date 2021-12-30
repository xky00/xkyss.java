package com.xkyss.core.util;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilTest {
    @Test
    public void test_01_parseInt() {
        Assert.assertEquals(11, NumberUtil.parseInt("11"));
        Assert.assertEquals(0x11, NumberUtil.parseInt("0x11"));
        Assert.assertEquals(0x11, NumberUtil.parseInt("0X11"));

        // 解析失败,返回默认值
        Assert.assertEquals(0, NumberUtil.parseInt("fff", 0));
    }
}
