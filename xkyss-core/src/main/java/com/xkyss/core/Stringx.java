package com.xkyss.core;

public class Stringx extends CharSequencex {

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
        if (isNullOrEmpty(str)) {
            return str;
        }

        if (isNullOrEmpty(cut)) {
            return str;
        }

        int length = cut.length();
        while (str.endsWith(cut)) {
            str = str.substring(0, str.length() - length);
        }
        return str;
    }
}
