package com.xkyss.mocky.unit.time;

import java.time.LocalTime;

public class LocalTimesImpl implements LocalTimes {

    @Override
    public LocalTime get() {
        return LocalTime.now();
    }

    @Override
    public LocalTimes gets() {
        return () -> {
            return LocalTime.ofSecondOfDay(get().toSecondOfDay() + 60);
        };
    }
}
