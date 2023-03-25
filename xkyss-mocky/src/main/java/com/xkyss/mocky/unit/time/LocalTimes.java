package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit;

import java.time.LocalTime;

public interface LocalTimes extends MockUnit<LocalTime> {

    default LocalTimes gets() {
        return this;
    }
}
