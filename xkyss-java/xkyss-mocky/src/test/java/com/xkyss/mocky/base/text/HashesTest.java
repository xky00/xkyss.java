package com.xkyss.mocky.base.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;

public class HashesTest {

    Hashes hashes;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        hashes = new Hashes(random, new Strings(random));
    }

    @Test
    public void test_md2() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.md2().matches("^[0-9a-f]+$"));
        }
    }

    @Test
    public void test_md5() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.md5().matches("^[0-9a-f]+$"));
        }
    }

    @Test
    public void test_sha1() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.sha1().matches("^[0-9a-f]+$"));
        }
    }

    @Test
    public void test_sha256() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.sha256().matches("^[0-9a-f]+$"));
        }
    }

    @Test
    public void test_sha384() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.sha384().matches("^[0-9a-f]+$"));
        }
    }

    @Test
    public void test_sha512() {
        for (int i = 0; i < TEST_COUNT; i++) {
            Assertions.assertTrue(hashes.sha512().matches("^[0-9a-f]+$"));
        }
    }
}
