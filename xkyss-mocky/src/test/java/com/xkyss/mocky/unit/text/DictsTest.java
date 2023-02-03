package com.xkyss.mocky.unit.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class DictsTest {

    Dicts dicts;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        dicts = new Dicts(random);
    }

    @Test
    public void test_get() {
        // resources/dicts/test 文件内容就一行: hello
        Assertions.assertEquals("hello", dicts.get("test"));
    }
}
