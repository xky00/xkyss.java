package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit2;

import java.time.LocalTime;

public interface LocalTimes extends MockUnit2<LocalTime> {
    default LocalTimes gets() {
        return this;
    }
}
