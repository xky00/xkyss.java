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
}
