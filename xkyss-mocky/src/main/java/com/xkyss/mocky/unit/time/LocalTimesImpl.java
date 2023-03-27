package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.AMockUnit;

import java.time.LocalTime;
import java.util.function.Supplier;

class LocalTimesImpl extends AMockUnit<LocalTime> implements LocalTimes {

    public LocalTimesImpl() {
        super(LocalTime::now);
    }

    protected LocalTimesImpl(Supplier<LocalTime> supplier) {
        super(supplier);
    }

    @Override
    public LocalTimes get1() {
        Supplier<LocalTime> supplier = () -> {
            LocalTime localTime = get();
            return LocalTime.ofSecondOfDay(localTime.toSecondOfDay() + 60);
        };
        return new LocalTimesImpl(supplier);
    }

    @Override
    public LocalTimes get2() {
        Supplier<LocalTime> supplier = () -> {
            LocalTime localTime = get();
            return LocalTime.ofSecondOfDay(localTime.toSecondOfDay() + 3600);
        };
        return new LocalTimesImpl(supplier);
    }
}
