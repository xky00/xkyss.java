package com.xkyss.mocky.unit.time.impl;

import com.xkyss.mocky.unit.time.LocalTimes;

import java.time.LocalTime;

public class LocalTimesImpl implements LocalTimes {

    @Override
    public LocalTimes gets() {
        return last -> {
            return LocalTime.ofSecondOfDay(last.toSecondOfDay() + 60);
        };
    }

    @Override
    public LocalTime apply(LocalTime localTime) {
        if (localTime == null) {
            return LocalTime.now();
        }
        else {
            return localTime;
        }
    }
}
