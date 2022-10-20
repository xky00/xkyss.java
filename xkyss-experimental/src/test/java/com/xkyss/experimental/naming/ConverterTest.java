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

    @Test
    public void test_toAda() {
       Assertions.assertEquals("God_Like_Dog", Converter.fromAuto("God.like.Dog").toAda());
       Assertions.assertEquals("God_Like_Dog", Converter.fromAuto("God likE DOG").toAda());
       Assertions.assertEquals("God_Like_Dog", Converter.fromAuto("God-likE-DOG").toAda());
       Assertions.assertEquals("God_Like_Dog", Converter.fromAuto("GOD-LIKE-DOG").toAda());
       Assertions.assertEquals("God_Like_Dog", Converter.fromAuto("god-like-dog").toAda());
    }

    @Test
    public void test_toCamel() {
       Assertions.assertEquals("godLikeDog", Converter.fromAuto("God.like.Dog").toCamel());
       Assertions.assertEquals("godLikeDog", Converter.fromAuto("God likE DOG").toCamel());
       Assertions.assertEquals("godLikeDog", Converter.fromAuto("God-likE-DOG").toCamel());
       Assertions.assertEquals("godLikeDog", Converter.fromAuto("GOD-LIKE-DOG").toCamel());
       Assertions.assertEquals("godLikeDog", Converter.fromAuto("god-like-dog").toCamel());
    }

    @Test
    public void test_toCobol() {
       Assertions.assertEquals("GOD-LIKE-DOG", Converter.fromAuto("God.like.Dog").toCobol());
       Assertions.assertEquals("GOD-LIKE-DOG", Converter.fromAuto("God likE DOG").toCobol());
       Assertions.assertEquals("GOD-LIKE-DOG", Converter.fromAuto("God-likE-DOG").toCobol());
       Assertions.assertEquals("GOD-LIKE-DOG", Converter.fromAuto("GOD-LIKE-DOG").toCobol());
       Assertions.assertEquals("GOD-LIKE-DOG", Converter.fromAuto("god-like-dog").toCobol());
    }

    @Test
    public void test_toMacro() {
        Assertions.assertEquals("GOD_LIKE_DOG", Converter.fromAuto("God.like.Dog").toMacro());
        Assertions.assertEquals("GOD_LIKE_DOG", Converter.fromAuto("God likE DOG").toMacro());
        Assertions.assertEquals("GOD_LIKE_DOG", Converter.fromAuto("God-likE-DOG").toMacro());
        Assertions.assertEquals("GOD_LIKE_DOG", Converter.fromAuto("GOD-LIKE-DOG").toMacro());
        Assertions.assertEquals("GOD_LIKE_DOG", Converter.fromAuto("god-like-dog").toMacro());
    }

    @Test
    public void test_toDot() {
       Assertions.assertEquals("god.like.dog", Converter.fromAuto("God.like.Dog").toDot());
       Assertions.assertEquals("god.like.dog", Converter.fromAuto("God likE DOG").toDot());
       Assertions.assertEquals("god.like.dog", Converter.fromAuto("God-likE-DOG").toDot());
       Assertions.assertEquals("god.like.dog", Converter.fromAuto("GOD-LIKE-DOG").toDot());
       Assertions.assertEquals("god.like.dog", Converter.fromAuto("god-like-dog").toDot());
    }

    @Test
    public void test_toKebab() {
       Assertions.assertEquals("god-like-dog", Converter.fromAuto("God.like.Dog").toKebab());
       Assertions.assertEquals("god-like-dog", Converter.fromAuto("God likE DOG").toKebab());
       Assertions.assertEquals("god-like-dog", Converter.fromAuto("God-likE-DOG").toKebab());
       Assertions.assertEquals("god-like-dog", Converter.fromAuto("GOD-LIKE-DOG").toKebab());
       Assertions.assertEquals("god-like-dog", Converter.fromAuto("god-like-dog").toKebab());
    }

    @Test
    public void test_toLower() {
       Assertions.assertEquals("god like dog", Converter.fromAuto("God.like.Dog").toLower());
       Assertions.assertEquals("god like dog", Converter.fromAuto("God likE DOG").toLower());
       Assertions.assertEquals("god like dog", Converter.fromAuto("God-likE-DOG").toLower());
       Assertions.assertEquals("god like dog", Converter.fromAuto("GOD-LIKE-DOG").toLower());
       Assertions.assertEquals("god like dog", Converter.fromAuto("god-like-dog").toLower());
    }

    @Test
    public void test_toPascal() {
       Assertions.assertEquals("GodLikeDog", Converter.fromAuto("God.like.Dog").toPascal());
       Assertions.assertEquals("GodLikeDog", Converter.fromAuto("God likE DOG").toPascal());
       Assertions.assertEquals("GodLikeDog", Converter.fromAuto("God-likE-DOG").toPascal());
       Assertions.assertEquals("GodLikeDog", Converter.fromAuto("GOD-LIKE-DOG").toPascal());
       Assertions.assertEquals("GodLikeDog", Converter.fromAuto("god-like-dog").toPascal());
    }

    @Test
    public void test_toSentence() {
       Assertions.assertEquals("God like dog", Converter.fromAuto("God.like.Dog").toSentence());
       Assertions.assertEquals("God like dog", Converter.fromAuto("God likE DOG").toSentence());
       Assertions.assertEquals("God like dog", Converter.fromAuto("God-likE-DOG").toSentence());
       Assertions.assertEquals("God like dog", Converter.fromAuto("GOD-LIKE-DOG").toSentence());
       Assertions.assertEquals("God like dog", Converter.fromAuto("god-like-dog").toSentence());
    }
}
