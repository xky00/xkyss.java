package com.xkyss.extensions.java.lang.Boolean;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

@SuppressWarnings("ALL")
@Extension
public class BooleanExt {
    /**
     * boolean值转为int
     *
     * @param value Boolean值
     * @return int值
     */
    @Extension
    public static int intValue(boolean value) {
        return value ? 1 : 0;
    }
    /**
     * Boolean值转为int
     *
     * @param self Boolean值
     * @return int值
     */
    public static int intValue(@This Boolean self) {
        return self ? 1 : 0;
    }

    /**
     * boolean值转为Integer
     *
     * @param value Boolean值
     * @return Integer值
     */
    @Extension
    public static Integer toInteger(boolean value) {
        return intValue(value);
    }

    /**
     * Boolean值转为Integer
     *
     * @param self Boolean值
     * @return Integer值
     */
    public static Integer toInteger(@This Boolean self) {
        return intValue(self);
    }

    /**
     * boolean值转为char
     *
     * @param value Boolean值
     * @return char值
     */
    @Extension
    public static char charValue(boolean value) {
        return (char) intValue(value);
    }

    /**
     * boolean值转为char
     *
     * @param self Boolean值
     * @return char值
     */
    public static char charValue(@This Boolean self) {
        return (char) intValue(self);
    }

    /**
     * boolean值转为Character
     *
     * @param value Boolean值
     * @return Character值
     */
    @Extension
    public static Character toCharacter(boolean value) {
        return charValue(value);
    }

    /**
     * boolean值转为Character
     *
     * @param self Boolean值
     * @return Character值
     */
    public static Character toCharacter(@This Boolean self) {
        return charValue(self);
    }

    /**
     * boolean值转为byte
     *
     * @param value Boolean值
     * @return byte值
     */
    @Extension
    public static byte byteValue(boolean value) {
        return (byte) intValue(value);
    }

    /**
     * boolean值转为byte
     *
     * @param self Boolean值
     * @return byte值
     */
    public static byte byteValue(@This Boolean self) {
        return (byte) intValue(self);
    }

    /**
     * boolean值转为Byte
     *
     * @param value Boolean值
     * @return Byte值
     */
    @Extension
    public static Byte toByte(boolean value) {
        return byteValue(value);
    }

    /**
     * boolean值转为Byte
     *
     * @param self Boolean值
     * @return Byte值
     */
    public static Byte toByte(@This Boolean self) {
        return toByte(self);
    }

    /**
     * boolean值转为long
     *
     * @param value Boolean值
     * @return long值
     */
    @Extension
    public static long longValue(boolean value) {
        return intValue(value);
    }

    /**
     * boolean值转为long
     *
     * @param self Boolean值
     * @return long值
     */
    public static long longValue(@This Boolean self) {
        return intValue(self);
    }

    /**
     * boolean值转为Long
     *
     * @param value Boolean值
     * @return Long值
     */
    @Extension
    public static Long toLong(boolean value) {
        return longValue(value);
    }

    /**
     * boolean值转为Long
     *
     * @param self Boolean值
     * @return Long值
     */
    public static Long toLong(@This Boolean self) {
        return longValue(self);
    }


    /**
     * boolean值转为short
     *
     * @param value Boolean值
     * @return short值
     */
    @Extension
    public static short shortValue(boolean value) {
        return (short) intValue(value);
    }

    /**
     * boolean值转为short
     *
     * @param self Boolean值
     * @return short值
     */
    public static short shortValue(@This Boolean self) {
        return (short) intValue(self);
    }

    /**
     * boolean值转为Short
     *
     * @param value Boolean值
     * @return Short值
     */
    @Extension
    public static Short toShort(boolean value) {
        return shortValue(value);
    }

    /**
     * boolean值转为Short
     *
     * @param self Boolean值
     * @return Short值
     */
    public static Short toShort(@This Boolean self) {
        return shortValue(self);
    }

    /**
     * boolean值转为float
     *
     * @param value Boolean值
     * @return float值
     */
    @Extension
    public static float floatValue(boolean value) {
        return (float) intValue(value);
    }

    /**
     * boolean值转为float
     *
     * @param self Boolean值
     * @return float值
     */
    public static float floatValue(@This Boolean self) {
        return (float) intValue(self);
    }
    /**
     * boolean值转为Float
     *
     * @param value Boolean值
     * @return Float值
     */
    @Extension
    public static Float toFloat(boolean value) {
        return floatValue(value);
    }

    /**
     * boolean值转为Float
     *
     * @param self Boolean值
     * @return Float值
     */
    public static Float toFloat(@This Boolean self) {
        return floatValue(self);
    }

    /**
     * boolean值转为double
     *
     * @param value Boolean值
     * @return double值
     */
    @Extension
    public static double doubleValue(boolean value) {
        return (double) intValue(value);
    }

    /**
     * boolean值转为float
     *
     * @param self Boolean值
     * @return double值
     */
    public static double doubleValue(@This Boolean self) {
        return (double) intValue(self);
    }

    /**
     * boolean值转为Double
     *
     * @param value Boolean值
     * @return Double值
     */
    @Extension
    public static Double toDouble(boolean value) {
        return doubleValue(value);
    }

    /**
     * boolean值转为Double
     *
     * @param self Boolean值
     * @return Double值
     */
    public static Double toDouble(@This Boolean self) {
        return doubleValue(self);
    }

    /**
     * 将boolean转换为字符串 {@code 'true'} 或者 {@code 'false'}.
     *
     * <pre>
     *   BooleanUtil.toStringTrueFalse(true)   = "true"
     *   BooleanUtil.toStringTrueFalse(false)  = "false"
     * </pre>
     *
     * @param value Boolean值
     * @return {@code 'true'}, {@code 'false'}
     */
    @Extension
    public static String toStringTrueFalse(boolean value) {
        return value ? "true" : "false";
    }

    /**
     * 将boolean转换为字符串 {@code 'true'} 或者 {@code 'false'}.
     *
     * <pre>
     *   BooleanUtil.toStringTrueFalse(true)   = "true"
     *   BooleanUtil.toStringTrueFalse(false)  = "false"
     * </pre>
     *
     * @param self Boolean值
     * @return {@code 'true'}, {@code 'false'}
     */
    public static String toStringTrueFalse(@This Boolean self) {
        return toStringTrueFalse(self.booleanValue());
    }

    /**
     * 将boolean转换为字符串 {@code 'on'} 或者 {@code 'off'}.
     *
     * <pre>
     *   BooleanUtil.toStringOnOff(true)   = "on"
     *   BooleanUtil.toStringOnOff(false)  = "off"
     * </pre>
     *
     * @param bool Boolean值
     * @return {@code 'on'}, {@code 'off'}
     */
    @Extension
    public static String toStringOnOff(boolean bool) {
        return toString(bool, "on", "off");
    }

    /**
     * 将boolean转换为字符串 {@code 'on'} 或者 {@code 'off'}.
     *
     * <pre>
     *   BooleanUtil.toStringOnOff(true)   = "on"
     *   BooleanUtil.toStringOnOff(false)  = "off"
     * </pre>
     *
     * @param self Boolean值
     * @return {@code 'on'}, {@code 'off'}
     */
    public static String toStringOnOff(@This Boolean self) {
        return toStringOnOff(self.booleanValue());
    }

    /**
     * 将boolean转换为字符串 {@code 'yes'} 或者 {@code 'no'}.
     *
     * <pre>
     *   BooleanUtil.toStringYesNo(true)   = "yes"
     *   BooleanUtil.toStringYesNo(false)  = "no"
     * </pre>
     *
     * @param bool Boolean值
     * @return {@code 'yes'}, {@code 'no'}
     */
    @Extension
    public static String toStringYesNo(boolean bool) {
        return toString(bool, "yes", "no");
    }

    /**
     * 将boolean转换为字符串 {@code 'yes'} 或者 {@code 'no'}.
     *
     * <pre>
     *   BooleanUtil.toStringYesNo(true)   = "yes"
     *   BooleanUtil.toStringYesNo(false)  = "no"
     * </pre>
     *
     * @param self Boolean值
     * @return {@code 'yes'}, {@code 'no'}
     */
    public static String toStringYesNo(@This Boolean self) {
        return toStringYesNo(self.booleanValue());
    }

    /**
     * 将boolean转换为字符串
     *
     * <pre>
     *   BooleanUtil.toString(true, "true", "false")   = "true"
     *   BooleanUtil.toString(false, "true", "false")  = "false"
     * </pre>
     *
     * @param bool Boolean值
     * @param trueString 当值为 {@code true}时返回此字符串, 可能为 {@code null}
     * @param falseString 当值为 {@code false}时返回此字符串, 可能为 {@code null}
     * @return 结果值
     */
    @Extension
    public static String toString(boolean bool, String trueString, String falseString) {
        return bool ? trueString : falseString;
    }

    /**
     * 将boolean转换为字符串
     *
     * <pre>
     *   BooleanUtil.toString(true, "true", "false")   = "true"
     *   BooleanUtil.toString(false, "true", "false")  = "false"
     * </pre>
     *
     * @param bool Boolean值
     * @param trueString 当值为 {@code true}时返回此字符串, 可能为 {@code null}
     * @param falseString 当值为 {@code false}时返回此字符串, 可能为 {@code null}
     * @return 结果值
     */
    public static String toString(@This Boolean bool, String trueString, String falseString) {
        return bool ? trueString : falseString;
    }
}
