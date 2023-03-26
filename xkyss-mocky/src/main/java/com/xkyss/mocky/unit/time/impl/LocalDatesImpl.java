package com.xkyss.mocky.unit.time.impl;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.unit.time.LocalDates;
import com.xkyss.mocky.unit.types.Ints;
import com.xkyss.mocky.unit.types.Longs;

import java.time.LocalDate;
import java.time.Month;

import static com.xkyss.core.util.Validate.notNull;
import static com.xkyss.mocky.contant.MockConsts.*;
import static java.time.LocalDate.*;
import static org.apache.commons.lang3.Validate.isTrue;

public class LocalDatesImpl implements LocalDates {

    public static final LocalDate EPOCH_START = ofEpochDay(0);
    private final Ints ints;
    private final Longs longs;

    public LocalDatesImpl(Ints ints, Longs longs) {
        this.ints = ints;
        this.longs = longs;
    }

    @Override
    public LocalDate get() {
        return between(EPOCH_START, now()).get();
    }

    @Override
    public MockUnit<LocalDate> between(LocalDate lowerDate, LocalDate upperDate) {
        notNull(lowerDate, "lowerDate");
        notNull(upperDate, "upperDate");
        isTrue(lowerDate.compareTo(upperDate)<0,
            LOWER_DATE_SMALLER_THAN_UPPER_DATE,
            "lower", lowerDate,
            "upper", upperDate);

        return () -> {

            long lowerEpoch = lowerDate.toEpochDay();
            long upperEpoch = upperDate.toEpochDay();
            long diff = upperEpoch - lowerEpoch;
            long randEpoch = longs.range(0L, diff).get();
            return ofEpochDay(lowerEpoch + randEpoch);
        };
    }

    @Override
    public MockUnit<LocalDate> thisYear() {
        return () -> {
            int year = now().getYear();
            int maxDays = now().lengthOfYear() + 1;
            int randDay = ints.range(1, maxDays).get();
            return ofYearDay(year, randDay);
        };
    }

    @Override
    public MockUnit<LocalDate> thisMonth() {
        return () -> {
            int year = now().getYear();
            Month month = now().getMonth();
            int lM = now().lengthOfMonth() + 1;
            int randDay = ints.range(1, lM).get();
            return of(year, month, randDay);
        };
    }

    @Override
    public MockUnit<LocalDate> future(LocalDate maxDate) {
        notNull(maxDate, "maxDate");
        isTrue(maxDate.compareTo(MAX.minusDays(1))<=0,
            MAX_DATE_NOT_BIGGER_THAN,
            "max", maxDate,
            "date", MAX.minusDays(1));
        isTrue(maxDate.plusDays(1).compareTo(now())>0,
            MAX_DATE_DIFFERENT_THAN_NOW,
            "max", maxDate,
            "now", now());
        return between(now().plusDays(1), maxDate.plusDays(1));
    }

    @Override
    public MockUnit<LocalDate> past(LocalDate minDate) {
        notNull(minDate,  "minDate");
        isTrue(minDate.compareTo(MIN)>0,
            MIN_DATE_BIGGER_THAN,
            "min", minDate,
            "date", MIN);
        isTrue(minDate.minusDays(1).compareTo(now())<0,
            MIN_DATE_DIFFERENT_THAN_NOW,
            "min", minDate,
            "now", now());
        return between(minDate, now());
    }
}
