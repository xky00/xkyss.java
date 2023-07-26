package com.xkyss.mocky.base.time;

import com.xkyss.mocky.abstraction.AMockUnit;
import com.xkyss.mocky.abstraction.MockUnit;

import java.time.LocalTime;
import java.util.function.Supplier;

public class LocalTimes extends AMockUnit<LocalTime> implements MockUnit<LocalTime> {

    public LocalTimes() {
        super(LocalTime::now);
    }

    protected LocalTimes(Supplier<LocalTime> supplier) {
        super(supplier);
    }

    public LocalTimes get1() {
        Supplier<LocalTime> supplier = () -> {
            LocalTime localTime = get();
            return LocalTime.ofSecondOfDay(localTime.toSecondOfDay() + 60);
        };
        return new LocalTimes(supplier);
    }

    public LocalTimes get2() {
        Supplier<LocalTime> supplier = () -> {
            LocalTime localTime = get();
            return LocalTime.ofSecondOfDay(localTime.toSecondOfDay() + 3600);
        };
        return new LocalTimes(supplier);
    }
}
