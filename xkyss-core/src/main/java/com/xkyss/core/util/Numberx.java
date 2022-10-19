package com.xkyss.core.util;

public class Numberx {
    public static boolean isAll(Number x, Number... numbers) {
        if (x == null) {
            return false;
        }

        for (Number n: numbers) {
            if (!x.equals(n)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllZero(Number... numbers) {
        for (Number n: numbers) {
            if (n != null && n.hashCode() != 0) {
                return false;
            }
        }

        return true;
    }
}
