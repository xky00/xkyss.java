package com.xkyss.core.util;

import com.xkyss.core.constant.StringPool;

/**
 * [String]工具类
 *
 * @author xkyii
 * @createdAt 2021/07/28.
 */
public class StringUtil extends CharSequenceUtil implements StringPool {

    /**
     * 判断对象是否null或者空字符串
     * @param obj 目标对象
     * @return true:是null或空字符串
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return 0 == ((CharSequence) obj).length();
        }
        return false;
    }
}
