package com.xkyss.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtilTest {

    @Test
    public void test_01() {
        Collection<String> c = new ArrayList<>();

        c.add("a");
        c.add("b");
        c.add("c");

        List<String> list = (List<String>) c;

        Assert.assertTrue(true);
    }

    @Test
    public void test_02_subtract() {
        List<Integer> list1 = ListUtil.toList(1, 2, 3, 4);
        List<Integer> list2 = ListUtil.toList(3, 4, 5, 6);

        List<Integer> ret = ListUtil.subtract(list1, list2);

        Assert.assertNotNull(ret);
        Assert.assertEquals(2, ret.size());
        Assert.assertEquals(1, ret.get(0).intValue());
        Assert.assertEquals(2, ret.get(1).intValue());
    }

    @Test
    public void test_03_subtract() {
        List<Integer> list1 = ListUtil.toList(1, 2, 3, 4);
        List<Integer> list2 = ListUtil.toList(3, 4, 5, 6);
    }
}
