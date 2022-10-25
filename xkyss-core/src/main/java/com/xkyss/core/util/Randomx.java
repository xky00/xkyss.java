package com.xkyss.core.util;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class Randomx {

    /**
     * Generates a random boolean value.
     *
     * @return the random boolean
     * @since 3.5
     */
    public static boolean nextBoolean() {
        return random().nextBoolean();
    }

    /**
     * Generates an array of random bytes.
     *
     * @param count
     *            the size of the returned array
     * @return the random byte array
     * @throws IllegalArgumentException if {@code count} is negative
     */
    public static byte[] nextBytes(final int count) {
        Checkx.isTrue(count >= 0, "Count cannot be negative.");

        final byte[] result = new byte[count];
        random().nextBytes(result);
        return result;
    }

    /**
     * Generates a random double within 0 - Double.MAX_VALUE.
     *
     * @return the random double
     * @see #nextDouble(double, double)
     * @since 3.5
     */
    public static double nextDouble() {
        return nextDouble(0, Double.MAX_VALUE);
    }

    /**
     * Generates a random double within the specified range.
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random double
     */
    public static double nextDouble(final double startInclusive, final double endExclusive) {
        Checkx.isTrue(endExclusive >= startInclusive,
            "Start value must be smaller or equal to end value.");
        Checkx.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + ((endExclusive - startInclusive) * random().nextDouble());
    }

    /**
     * Generates a random float within 0 - Float.MAX_VALUE.
     *
     * @return the random float
     * @see #nextFloat(float, float)
     * @since 3.5
     */
    public static float nextFloat() {
        return nextFloat(0, Float.MAX_VALUE);
    }

    /**
     * Generates a random float within the specified range.
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random float
     */
    public static float nextFloat(final float startInclusive, final float endExclusive) {
        Checkx.isTrue(endExclusive >= startInclusive,
            "Start value must be smaller or equal to end value.");
        Checkx.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + ((endExclusive - startInclusive) * random().nextFloat());
    }

    /**
     * Generates a random int within 0 - Integer.MAX_VALUE.
     *
     * @return the random integer
     * @see #nextInt(int, int)
     * @since 3.5
     */
    public static int nextInt() {
        return nextInt(0, Integer.MAX_VALUE);
    }

    /**
     * Generates a random integer within the specified range.
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random integer
     */
    public static int nextInt(final int startInclusive, final int endExclusive) {
        Checkx.isTrue(endExclusive >= startInclusive,
            "Start value must be smaller or equal to end value.");
        Checkx.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + random().nextInt(endExclusive - startInclusive);
    }

    /**
     * Generates a random long within 0 - Long.MAX_VALUE.
     *
     * @return the random long
     * @see #nextLong(long, long)
     * @since 3.5
     */
    public static long nextLong() {
        return nextLong(Long.MAX_VALUE);
    }

    /**
     * Generates a {@code long} value between 0 (inclusive) and the specified
     * value (exclusive).
     *
     * @param n Bound on the random number to be returned.  Must be positive.
     * @return a random {@code long} value between 0 (inclusive) and {@code n}
     * (exclusive).
     */
    private static long nextLong(final long n) {
        // Extracted from o.a.c.rng.core.BaseProvider.nextLong(long)
        long bits;
        long val;
        do {
            bits = random().nextLong() >>> 1;
            val  = bits % n;
        } while (bits - val + (n - 1) < 0);

        return val;
    }

    /**
     * Generates a random long within the specified range.
     *
     * @param startInclusive
     *            the smallest value that can be returned, must be non-negative
     * @param endExclusive
     *            the upper bound (not included)
     * @throws IllegalArgumentException
     *             if {@code startInclusive > endExclusive} or if
     *             {@code startInclusive} is negative
     * @return the random long
     */
    public static long nextLong(final long startInclusive, final long endExclusive) {
        Checkx.isTrue(endExclusive >= startInclusive,
            "Start value must be smaller or equal to end value.");
        Checkx.isTrue(startInclusive >= 0, "Both range values must be non-negative.");

        if (startInclusive == endExclusive) {
            return startInclusive;
        }

        return startInclusive + nextLong(endExclusive - startInclusive);
    }

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }
}