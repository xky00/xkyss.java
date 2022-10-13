package com.xkyss.core.util;

import java.util.Calendar;
import java.util.Date;

public class Datex {
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR)) {
            return false;
        }
        if (c1.get(Calendar.DAY_OF_YEAR) != c2.get(Calendar.DAY_OF_YEAR)) {
            return false;
        }

        return true;
    }
}
