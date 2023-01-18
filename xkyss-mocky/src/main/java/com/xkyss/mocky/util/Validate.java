package com.xkyss.mocky.util;

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
            throw new NullPointerException(String.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(String.format(fmt, params));
    }

    public static void notEmpty(double[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(String.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(String.format(fmt, params));
    }

    public static void notEmpty(float[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(String.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(String.format(fmt, params));
    }
    public static void notEmpty(int[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(String.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(String.format(fmt, params));
    }

    public static void notEmpty(long[] array, String fmt, Object... params) {
        if (null==array)
            throw new NullPointerException(String.format(fmt, params));
        if (0==array.length)
            throw new IllegalArgumentException(String.format(fmt, params));
    }
}
