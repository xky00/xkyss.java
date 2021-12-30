package com.xkyss.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;

public class NumberUtil {

    /// @region isEmpty

    public static boolean isEmpty(byte in) {
        return in == 0;
    }

    public static boolean isEmpty(short in) {
        return in == 0;
    }

    public static boolean isEmpty(int in) {
        return in == 0;
    }

    public static boolean isEmpty(long in) {
        return in == 0;
    }

    public static boolean isEmpty(float in) {
        return in == 0;
    }

    public static boolean isEmpty(double in) {
        return in == 0;
    }

    public static boolean isEmpty(Number in) {
        return in == null || in.intValue() == 0;
    }

    public static boolean isEmpty(BigInteger in) {
        return in == null || in.equals(BigInteger.ZERO);
    }

    public static boolean isEmpty(BigDecimal in) {
        return in == null || in.equals(BigDecimal.ZERO);
    }

    /// @endregion isEmpty

    /// @region parse

    /**
     * 解析转换数字字符串为byte型数字，规则如下：
     *
     * <pre>
     * 1、0x开头的视为16进制数字
     * 2、0开头的忽略开头的0
     * 3、其它情况按照10进制转换
     * 4、空串返回0
     * 5、.123形式返回0（按照小于0的小数对待）
     * 6、123.56截取小数点之前的数字，忽略小数部分
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return byte
     * @since 4.1.4
     */
    public static byte parseByte(String number) throws NumberFormatException {
        if (StringUtil.isBlank(number)) {
            return 0;
        }

        if (StringUtil.startWithIgnoreCase(number, "0x")) {
            // 0x04表示16进制数
            return Byte.parseByte(number.substring(2), 16);
        }

        try {
            return Byte.parseByte(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).byteValue();
        }
    }

    /**
     * 解析转换数字字符串为byte型数字：
     * 规则同:
     * @see NumberUtil#parseByte(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return int
     */
    public static byte parseByte(String number, byte defaultValue) {
        try {
            return parseByte(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换数字字符串为short型数字，规则如下：
     *
     * <pre>
     * 1、0x开头的视为16进制数字
     * 2、0开头的忽略开头的0
     * 3、其它情况按照10进制转换
     * 4、空串返回0
     * 5、.123形式返回0（按照小于0的小数对待）
     * 6、123.56截取小数点之前的数字，忽略小数部分
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return byte
     * @since 4.1.4
     */
    public static short parseShort(String number) throws NumberFormatException {
        if (StringUtil.isBlank(number)) {
            return 0;
        }

        if (StringUtil.startWithIgnoreCase(number, "0x")) {
            // 0x04表示16进制数
            return Short.parseShort(number.substring(2), 16);
        }

        try {
            return Short.parseShort(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).byteValue();
        }
    }

    /**
     * 解析转换数字字符串为short型数字：
     * 规则同:
     * @see NumberUtil#parseShort(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return int
     */
    public static short parseShort(String number, short defaultValue) {
        try {
            return parseByte(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换数字字符串为int型数字，规则如下：
     *
     * <pre>
     * 1、0x开头的视为16进制数字
     * 2、0开头的忽略开头的0
     * 3、其它情况按照10进制转换
     * 4、空串返回0
     * 5、.123形式返回0（按照小于0的小数对待）
     * 6、123.56截取小数点之前的数字，忽略小数部分
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return int
     * @throws NumberFormatException 数字格式异常
     * @since 4.1.4
     */
    public static int parseInt(String number) throws NumberFormatException {
        if (StringUtil.isBlank(number)) {
            return 0;
        }

        if (StringUtil.startWithIgnoreCase(number, "0x")) {
            // 0x04表示16进制数
            return Integer.parseInt(number.substring(2), 16);
        }

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).intValue();
        }
    }

    /**
     * 解析转换数字字符串为int型数字：
     * 规则同:
     * @see NumberUtil#parseInt(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return int
     */
    public static int parseInt(String number, int defaultValue) {
        try {
            return parseInt(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换数字字符串为long型数字，规则如下：
     *
     * <pre>
     * 1、0x开头的视为16进制数字
     * 2、0开头的忽略开头的0
     * 3、空串返回0
     * 4、其它情况按照10进制转换
     * 5、.123形式返回0（按照小于0的小数对待）
     * 6、123.56截取小数点之前的数字，忽略小数部分
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return long
     * @since 4.1.4
     */
    public static long parseLong(String number) {
        if (StringUtil.isBlank(number)) {
            return 0L;
        }

        if (StringUtil.startWithIgnoreCase(number, "0x")) {
            // 0x04表示16进制数
            return Long.parseLong(number.substring(2), 16);
        }

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).longValue();
        }
    }

    /**
     * 解析转换数字字符串为long型数字：
     * 规则同:
     * @see NumberUtil#parseLong(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return long
     */
    public static long parseLong(String number, long defaultValue) {
        try {
            return parseInt(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换数字字符串为float型数字，规则如下：
     *
     * <pre>
     * 1、0开头的忽略开头的0
     * 2、空串返回0
     * 3、其它情况按照10进制转换
     * 4、.123形式返回0.123（按照小于0的小数对待）
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return long
     * @since 5.5.5
     */
    public static float parseFloat(String number) {
        if (StringUtil.isBlank(number)) {
            return 0f;
        }

        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).floatValue();
        }
    }


    /**
     * 解析转换数字字符串为float型数字：
     * 规则同:
     * @see NumberUtil#parseFloat(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return long
     */
    public static float parseFloat(String number, float defaultValue) {
        try {
            return parseInt(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析转换数字字符串为double型数字，规则如下：
     *
     * <pre>
     * 1、0开头的忽略开头的0
     * 2、空串返回0
     * 3、其它情况按照10进制转换
     * 4、.123形式返回0.123（按照小于0的小数对待）
     * </pre>
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @return long
     * @since 5.5.5
     */
    public static double parseDouble(String number) {
        if (StringUtil.isBlank(number)) {
            return 0D;
        }

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return parseNumber(number).doubleValue();
        }
    }

    /**
     * 解析转换数字字符串为double型数字：
     * 规则同:
     * @see NumberUtil#parseDouble(String)
     *
     * @param number 数字，支持0x开头、0开头和普通十进制
     * @param defaultValue 如果解析失败,就返回默认值
     * @return long
     */
    public static double parseDouble(String number, double defaultValue) {
        try {
            return parseInt(number);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 将指定字符串转换为{@link Number} 对象
     *
     * @param numberStr Number字符串
     * @return Number对象
     * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
     * @since 4.1.15
     */
    public static Number parseNumber(String numberStr) throws NumberFormatException {
        try {
            return NumberFormat.getInstance().parse(numberStr);
        } catch (ParseException e) {
            final NumberFormatException nfe = new NumberFormatException(e.getMessage());
            nfe.initCause(e);
            throw nfe;
        }
    }

    /// @endregion parse
}
