package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.unit.time.impl.LocalTimesImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class LocalTimesTest {

    @Test
    public void test_01() {
        LocalTimes times = new LocalTimesImpl();
        LocalTime t1 = times.get();
        LocalTime t2 = times.gets().get();
        System.out.println(t1);
        System.out.println(t2);

    }
}
