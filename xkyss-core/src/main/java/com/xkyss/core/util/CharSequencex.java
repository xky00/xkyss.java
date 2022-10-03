package com.xkyss.core.util;

import com.xkyss.core.constant.StringPool;

public class CharSequencex implements StringPool {
    /**
     * 判断对象是否null或者空字符串
     * @param s 目标对象
     * @return true:是null或空字符串
     */
    public static boolean isNullOrEmpty(Object s) {
        if (s == null) {
            return true;
        }

        if (s instanceof CharSequence) {
            return ((CharSequence) s).length() == 0;
        }

        return false;
    }

    /**
     * 判断字符串是否为空
     * @param s 字符串
     * @return true:是null或空字符串
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }
}
