package com.xkyss.core.util;

import static java.util.stream.IntStream.range;

public class Validate {
    public static void isFinite(Double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(String.format("Number %f should be finite (non-infinite, non-nan).", value));
        }
    }

    public static void isFinite(Float value) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            throw new IllegalArgumentException(String.format("Number %f should be finite (non-infinite, non-nan).", value));
        }
    }


    public static <T> void notEmpty(T[] arr, String name) {
        String message = String.format("Input parameter: '%s' should not be empty or NULL.", name);
        if (null == arr)
            throw new NullPointerException(message);
        if (0 == arr.length) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(char[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(Stringx.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(Stringx.format(fmt, params));
    }

    public static void notEmpty(double[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(Stringx.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(Stringx.format(fmt, params));
    }

    public static void notEmpty(float[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(Stringx.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(Stringx.format(fmt, params));
    }
    public static void notEmpty(int[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(Stringx.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(Stringx.format(fmt, params));
    }

    public static void notEmpty(long[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(Stringx.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(Stringx.format(fmt, params));
    }

    public static <T extends CharSequence> T notEmpty(T chars, String fmt, Object... params) {
        if (chars == null) {
            throw new NullPointerException(Stringx.format(fmt, params));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(Stringx.format(fmt, params));
        }
        return chars;
    }

    public static <T extends CharSequence> void notEmpty(T chars, String input) {

        String msg = String.format("Input parameter: '%s' should not be empty or NULL.", input);

        if (chars == null) {
            throw new NullPointerException(msg);
        }

        if (chars.length() == 0) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static <T> void notNull(T object, final String fmt, Object... params) {
        if (object == null)
            throw new NullPointerException(Stringx.format(fmt, params));
    }


    public static <T> void notNull(T object, String input) {
        notNull(object, "Input parameter: '{}' should not be NULL.", input);
    }

    public static <T> void notEmptyOrNullValues(T[] arr, String arrName) {
        notEmpty(arr, arrName);
        range(0, arr.length).forEach(i  -> notNull(arr[i], arrName + "[" + i + "]"));
    }

    public static void notEmptyOrNullValues(String[] arr, String arrName) {
        notEmpty(arr, arrName);
        range(0, arr.length).forEach(i  -> notEmpty(arr[i], arrName + "[" + i + "]"));
    }
}
