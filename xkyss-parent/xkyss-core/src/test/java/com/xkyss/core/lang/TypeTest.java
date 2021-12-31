package com.xkyss.core.lang;

import org.junit.Assert;
import org.junit.Test;

public class TypeTest {

    @Test
    public void test_Wrap() {
        System.out.println(byte.class.getName()); // byte
        System.out.println(Byte.class.getName()); // java.lang.Byte

        Assert.assertNotEquals(byte.class, Byte.class);
        Assert.assertNotEquals(char.class, Character.class);
    }
}
