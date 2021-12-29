package com.xkyss.extensions.java.util.Arrays;

import manifold.ext.rt.api.Extension;

@Extension
public class ArraysExt {

    @SafeVarargs
    @Extension
    public static <T> T[] of(T... values) {
        return values;
    }


    // ---------------------------------------------------------------------- addAll

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static byte[] addAll(byte[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (byte[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final byte[] result = new byte[length];
        length = 0;
        for (byte[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static int[] addAll(int[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (int[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final int[] result = new int[length];
        length = 0;
        for (int[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static long[] addAll(long[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (long[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final long[] result = new long[length];
        length = 0;
        for (long[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static double[] addAll(double[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (double[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final double[] result = new double[length];
        length = 0;
        for (double[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static float[] addAll(float[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (float[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final float[] result = new float[length];
        length = 0;
        for (float[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static char[] addAll(char[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (char[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final char[] result = new char[length];
        length = 0;
        for (char[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static boolean[] addAll(boolean[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (boolean[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final boolean[] result = new boolean[length];
        length = 0;
        for (boolean[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }

    /**
     * 将多个数组合并在一起<br>
     * 忽略null的数组
     *
     * @param arrays 数组集合
     * @return 合并后的数组
     * @since 4.6.9
     */
    @Extension
    public static short[] addAll(short[]... arrays) {
        if (arrays.length == 1) {
            return arrays[0];
        }

        // 计算总长度
        int length = 0;
        for (short[] array : arrays) {
            if (null != array) {
                length += array.length;
            }
        }

        final short[] result = new short[length];
        length = 0;
        for (short[] array : arrays) {
            if (null != array) {
                System.arraycopy(array, 0, result, length, array.length);
                length += array.length;
            }
        }
        return result;
    }
}
