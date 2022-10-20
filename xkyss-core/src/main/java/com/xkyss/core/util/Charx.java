package com.xkyss.core.util;

public class Charx {

    private static final String[] CHAR_STRING_ARRAY = new String[128];

    static {
        Arrayx.setAll(CHAR_STRING_ARRAY, i -> String.valueOf((char) i));
    }

    public static String toString(final char ch) {
        if (ch < CHAR_STRING_ARRAY.length) {
            return CHAR_STRING_ARRAY[ch];
        }
        return String.valueOf(ch);
    }
}
