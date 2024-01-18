package com.xkyss.mocky.base.time;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class LocalTimesTest {

    @Test
    public void test_01() {
        LocalTimes times = new LocalTimes();
        LocalTime t1 = times.get();
        LocalTime t2 = times.get1().get();
        LocalTime t3 = times.get1().get2().get();
        System.out.println(t1);
        System.out.println(t2);
        System.out.println(t3);
    }
}
