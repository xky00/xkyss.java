package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;


public class ListxTest {
    @Test
    public void test_get() {
        List<Integer> list = ArrayListx.of(1, 2, 3, 4, 5);
        Assertions.assertEquals(1, Listx.get(list, 0));
        Assertions.assertEquals(2, Listx.get(list, 1));
        Assertions.assertEquals(3, Listx.get(list, 2));
        Assertions.assertEquals(4, Listx.get(list, 3));
        Assertions.assertEquals(5, Listx.get(list, 4));
        Assertions.assertEquals(null, Listx.get(list, 5));
    }
}
