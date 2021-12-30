package com.xkyss.core.util;

import org.junit.Assert;
import org.junit.Test;

public class ArrayUtilTest {

    @Test
    public void test_01_isEmpty() {
        int[] ints = new int[] {};
        // 暂不支持基础类型
//        ArrayUtil.isEmpty(ints);

        Assert.assertTrue(ArrayUtil.isEmpty(new Integer[] {}));
        Assert.assertTrue(ArrayUtil.isEmpty(new Integer[0]));
    }

    @Test
    public void test_02_addAll_int() {
        int[] ints = new int[] { 1, 2, 3 };
        int[] ints2 = new int[] { 4, 5, 6 };

//        ArrayUtil.addAll(ints, ints2);
    }

    @Test
    public void test_03_addAll_Integer() {
        Integer[] ints = new Integer[] { 1, 2, 3 };
        Integer[] ints2 = new Integer[] { 4, 5, 6 };

        Integer[] result = ArrayUtil.addAll(ints, ints2);

        Assert.assertEquals(6, result.length);
    }
}
