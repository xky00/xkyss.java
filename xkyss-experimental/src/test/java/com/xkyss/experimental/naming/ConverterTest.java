package com.xkyss.experimental.naming;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConverterTest {
    @Test
    public void test_fromAuto_1() {
        Converter c = Converter.fromAuto("t_user_info");
        Assertions.assertEquals("t_user_info", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"t", "user", "info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_2() {
        Converter c = Converter.fromAuto("userInfo");
        Assertions.assertEquals("userInfo", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"user", "Info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_3() {
        Converter c = Converter.fromAuto("UserInfo");
        Assertions.assertEquals("UserInfo", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"User", "Info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_4() {
        Converter c = Converter.fromAuto("User_Info");
        Assertions.assertEquals("User_Info", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"User", "Info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_5() {
        Converter c = Converter.fromAuto("User Info");
        Assertions.assertEquals("User Info", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"User", "Info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_6() {
        Converter c = Converter.fromAuto("User-Info");
        Assertions.assertEquals("User-Info", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"User", "Info"}, c.getWords());
    }

    @Test
    public void test_fromAuto_7() {
        Converter c = Converter.fromAuto("User.Info");
        Assertions.assertEquals("User.Info", c.getOriginal());
        Assertions.assertArrayEquals(new String[] {"User", "Info"}, c.getWords());
    }
}
