package com.xkyss.mocky.unit.time;

import com.xkyss.mocky.unit.types.Ints;
import com.xkyss.mocky.unit.types.Longs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static com.xkyss.mocky.unit.time.LocalDatesImpl.EPOCH_START;
import static java.time.LocalDate.MAX;
import static java.time.LocalDate.MIN;

public class LocalDatesTest {

    LocalDates localDates;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        localDates = LocalDates.defaultOf(Ints.defaultWith(random), Longs.defaultWith(random));
    }

    @Test
    public void testLocalDate() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.get();
            Assertions.assertTrue(!d.isBefore(EPOCH_START));
            Assertions.assertTrue(d.isBefore(LocalDate.now()));
        }
    }

    @Test
    public void testThisYear() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.thisYear().get();
            Assertions.assertEquals(d.getYear(), LocalDate.now().getYear() );
        }
    }

    @Test
    public void testThisMonth() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.thisMonth().get();
            Assertions.assertEquals(d.getYear(), LocalDate.now().getYear() );
            Assertions.assertEquals(d.getMonth(), LocalDate.now().getMonth() );
        }
    }

    @Test
    public void testBetweenNullLower() {
        Assertions.assertThrows(NullPointerException.class,
            () -> localDates.between(null, LocalDate.now()).get());
    }

    @Test
    public void testBetweenNullUpper() {
        Assertions.assertThrows(NullPointerException.class,
            () -> localDates.between(LocalDate.now(), null).get());
    }

    @Test
    public void testBetweenEqualLowerAndUpper() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.between(LocalDate.now(), LocalDate.now()).get());
    }

    @Test
    public void testBetweenLowerBiggerThanUpper() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.between(tomorrow, LocalDate.now()).get());
    }

    @Test
    public void testBetween() {
        LocalDate lastCentury = LocalDate.ofYearDay(1900, 100);
        LocalDate nextCentury = LocalDate.ofYearDay(2150, 100);

        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.between(lastCentury, nextCentury).get();
            Assertions.assertTrue(!d.isBefore(lastCentury));
            Assertions.assertTrue(d.isBefore(nextCentury));
        }
    }

    @Test
    public void testBetweenSingleDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.between(LocalDate.now(), tomorrow).get();
            Assertions.assertTrue(d.isEqual(LocalDate.now()));
        }
    }

    @Test
    public void testFutureNull() {
        Assertions.assertThrows(NullPointerException.class,
            () -> localDates.future(null).get());
    }

    @Test
    public void testFutureMaxDate() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.future(MAX).get());
    }

    @Test
    public void testFutureMaxMinus1Date() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.future(MAX.minusDays(1)).get();
            Assertions.assertTrue(
                d.isAfter(LocalDate.now()) && !d.isAfter(MAX.minusDays(1)));
        }
    }

    @Test
    public void testFuturePast() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.future(LocalDate.now().minusDays(5)).get());
    }

    @Test
    public void testFuture() {
        LocalDate fiveDaysInFuture = LocalDate.now().plusDays(5);
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.future(fiveDaysInFuture).get();
            Assertions.assertTrue(d.isAfter(LocalDate.now()) && !d.isAfter(fiveDaysInFuture));
        }
    }

    @Test
    public void testPastNull() {
        Assertions.assertThrows(NullPointerException.class,
            () -> localDates.past(null).get());
    }

    @Test
    public void testPastMinDate() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.past(MIN).get());
    }

    @Test
    public void testPastMinPlus1Date() {
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.past(MIN.plusDays(1)).get();
            Assertions.assertTrue(d.isBefore(LocalDate.now()) && !d.isBefore(MIN.plusDays(1)));
        }
    }

    @Test
    public void testPastFuture() {
        LocalDate future = LocalDate.now().plusDays(5);
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> localDates.past(future).get());
    }

    @Test
    public void testPast() {
        LocalDate fiveDaysInThePast = LocalDate.now().minusDays(5);
        for (int i = 0; i < TEST_COUNT; i++) {
            LocalDate d = localDates.past(fiveDaysInThePast).get();
            Assertions.assertTrue(!d.isBefore(fiveDaysInThePast) && !d.isAfter(LocalDate.now()));
        }
    }
}
