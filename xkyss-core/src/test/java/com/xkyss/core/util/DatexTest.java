package com.xkyss.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class DatexTest {
    @Test
    public void test_isSameDay() {
        Assertions.assertTrue(Datex.isSameDay(new Date(), new Date()));
    }
}
