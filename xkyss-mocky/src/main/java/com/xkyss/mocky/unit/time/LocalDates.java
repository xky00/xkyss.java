package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.unit.types.Ints;
import com.xkyss.mocky.unit.types.Longs;
import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDate;

public interface LocalDates extends MockUnit<LocalDate> {

    static LocalDates defaultOf(Ints ints, Longs longs) {
        return new LocalDatesImpl(ints, longs);
    }

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
