package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit;

import java.time.LocalTime;

public interface LocalTimes extends MockUnit<LocalTime> {
    default LocalTimes get1() {
        return this;
    }
    default LocalTimes get2() {
        return this;
    }
}
