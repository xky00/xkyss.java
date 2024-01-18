package com.xkyss.mocky.unit.address;

import com.xkyss.mocky.model.Province;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ProvincesTest {

    Provinces provincs;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        provincs = new Provinces(random);
    }

    @Test
    public void test_01() {
        Province province = provincs.get();
        Assertions.assertNotNull(province);
    }
}
