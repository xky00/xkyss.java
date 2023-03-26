package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDate;

public interface LocalDates extends MockUnit<LocalDate> {

    default MockUnit<LocalDate> between(LocalDate lowerDate, LocalDate upperDate) {
        throw new NotImplementedException();
    }

    default MockUnit<LocalDate> thisYear() {
        throw new NotImplementedException();
    }

    default MockUnit<LocalDate> thisMonth() {
        throw new NotImplementedException();
    }

    default MockUnit<LocalDate> future(LocalDate maxDate) {
        throw new NotImplementedException();
    }

    default MockUnit<LocalDate> past(LocalDate minDate) {
        throw new NotImplementedException();
    }
}
