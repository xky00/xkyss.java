package com.xkyss.core.util;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Stringx {
    /**
     * 按长度切分字符串
     * @param cs 源字符串
     * @param length 每个子串的长度
     * @return 字串数组
     */
    public static String[] splitByLength(final String cs, int length) {
        if (isEmpty(cs)) {
            return new String[] { cs };
        }

        int csLength = cs.length();
        int size = Numberx.ceil(csLength, length);
        String[] ret = new String[size];

        for (int i=0; i<size; i++) {
            int remain = csLength - length * (i + 1);
            int end = (remain > 0) ? ((i+1)*length) : csLength;
            ret[i] = cs.substring(i*length, end);
        }

        return ret;
    }

    /**
     * 从后开始截断
     *
     * <code>postSub("43010000", "00") == "4301"</code>
     * <code>postSub("430100000", "00") == "43010"</code>
     * <code>postSub("430100000", "0") == "4301"</code>
     *
     * @param str 源字符串
     * @param cut 被截断字符串
     * @return 被截断之后的字符串
     */
    public static String postSub(String str, String cut) {
        if (isEmpty(str)) {
            return str;
        }

        if (isEmpty(cut)) {
            return str;
        }

        int length = cut.length();
        while (str.endsWith(cut)) {
            str = str.substring(0, str.length() - length);
        }
        return str;
    }

    /**
     * 以[Slf4j MessageFormatter]样式格式化字符串
     * @param pattern 字符串模板
     * @param arg 单个参数
     * @return 格式化后的字符串
     */
    public static String format(String pattern, Object... arg) {
        if (arg == null) {
            return org.slf4j.helpers.MessageFormatter.format(pattern, arg).getMessage();
        }

        if (arg.length == 0) {
            return pattern;
        }

        if (arg.length == 1) {
            return org.slf4j.helpers.MessageFormatter.format(pattern, arg[0]).getMessage();
        }

        if (arg.length == 2) {
            return org.slf4j.helpers.MessageFormatter.format(pattern, arg[0], arg[1]).getMessage();
        }

        return org.slf4j.helpers.MessageFormatter.arrayFormat(pattern, arg).getMessage();
    }

    /**
     * 以[Slf4j MessageFormatter]样式格式化字符串
     * @param pattern 字符串模板
     * @param arg 数组参数
     * @param <T> 数组item
     * @return 格式化后的字符串
     */
    public static <T> String arrayFormat(String pattern, T[] arg) {
        return org.slf4j.helpers.MessageFormatter.arrayFormat(pattern, arg).getMessage();
    }

}
