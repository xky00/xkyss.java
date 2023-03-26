package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit2;

import java.time.LocalDate;

public interface LocalDates extends MockUnit2<LocalDate> {

    default LocalDates between(LocalDate lowerDate, LocalDate upperDate) {
        return this;
    }

    default LocalDates thisYear() {
        return this;
    }

    default LocalDates thisMonth() {
        return this;
    }

    default LocalDates future(LocalDate maxDate) {
        return this;
    }

    default LocalDates past(LocalDate minDate) {
        return this;
    }
}
