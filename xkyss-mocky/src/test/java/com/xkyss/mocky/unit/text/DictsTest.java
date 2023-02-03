package com.xkyss.mocky.unit.text;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;

public class DictsTest {

    Dicts dicts;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        dicts = new Dicts(random);
    }

    @Test
    public void test_get() {
        List<String> lines = Lists.newArrayList("hello", "world", "dicts");
        // resources/dicts/test 文件内容就一行: hello
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(lines.contains(dicts.get("test")));
        }
    }
}
