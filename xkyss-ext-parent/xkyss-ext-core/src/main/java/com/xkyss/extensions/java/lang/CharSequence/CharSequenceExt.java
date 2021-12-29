package com.xkyss.extensions.java.lang.CharSequence;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SuppressWarnings("ALL")
@Extension
public class CharSequenceExt {

    /**
     * <p>字符串是否为空白，空白的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isBlank(null)     // true}</li>
     *     <li>{@code StrUtil.isBlank("")       // true}</li>
     *     <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
     *     <li>{@code StrUtil.isBlank("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isEmpty(CharSequence)} 的区别是：
     * 该方法会校验空白字符，且性能相对于 {@link #isEmpty(CharSequence)} 略慢。</p>
     * <br>
     *
     * <p>建议：</p>
     * <ul>
     *     <li>该方法建议仅对于客户端（或第三方接口）传入的参数使用该方法。</li>
     *     <li>需要同时校验多个字符串时，建议采用 {@link #hasBlank(CharSequence...)} 或 {@link #isAllBlank(CharSequence...)}</li>
     * </ul>
     *
     * @param str 被检测的字符串
     * @return 若为空白，则返回 true
     * @see #isEmpty(CharSequence)
     */
    public static boolean isBlank(@This CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!Character.isBlank(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>字符串是否为非空白，非空白的定义如下： </p>
     * <ol>
     *     <li>不为 {@code null}</li>
     *     <li>不为空字符串：{@code ""}</li>
     *     <li>不为空格、全角空格、制表符、换行符，等不可见字符</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isNotBlank(null)     // false}</li>
     *     <li>{@code StrUtil.isNotBlank("")       // false}</li>
     *     <li>{@code StrUtil.isNotBlank(" \t\n")  // false}</li>
     *     <li>{@code StrUtil.isNotBlank("abc")    // true}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isNotEmpty(CharSequence)} 的区别是：
     * 该方法会校验空白字符，且性能相对于 {@link #isNotEmpty(CharSequence)} 略慢。</p>
     * <p>建议：仅对于客户端（或第三方接口）传入的参数使用该方法。</p>
     *
     * @param self 被检测的字符串
     * @return 是否为非空
     * @see #isBlank(CharSequence)
     */
    public static boolean isNotBlank(@This CharSequence self) {
        return !isBlank(self);
    }


    /**
     * <p>字符串是否为空，空的定义如下：</p>
     * <ol>
     *     <li>{@code null}</li>
     *     <li>空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isEmpty(null)     // true}</li>
     *     <li>{@code StrUtil.isEmpty("")       // true}</li>
     *     <li>{@code StrUtil.isEmpty(" \t\n")  // false}</li>
     *     <li>{@code StrUtil.isEmpty("abc")    // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
     * <p>建议：</p>
     * <ul>
     *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
     *     <li>需要同时校验多个字符串时，建议采用 {@link #hasEmpty(CharSequence...)} 或 {@link #isAllEmpty(CharSequence...)}</li>
     * </ul>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     * @see #isBlank(CharSequence)
     */
    public static boolean isEmpty(@This CharSequence str) {
        return str == null || str.length() == 0;
    }


    /**
     * <p>字符串是否为非空白，非空白的定义如下： </p>
     * <ol>
     *     <li>不为 {@code null}</li>
     *     <li>不为空字符串：{@code ""}</li>
     * </ol>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isNotEmpty(null)     // false}</li>
     *     <li>{@code StrUtil.isNotEmpty("")       // false}</li>
     *     <li>{@code StrUtil.isNotEmpty(" \t\n")  // true}</li>
     *     <li>{@code StrUtil.isNotEmpty("abc")    // true}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isNotBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
     * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
     *
     * @param str 被检测的字符串
     * @return 是否为非空
     * @see #isEmpty(CharSequence)
     */
    public static boolean isNotEmpty(@This CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 判断对象是否null或者空字符串
     * @param obj 目标对象
     * @return true:是null或空字符串
     */
    public static boolean isNullOrEmpty(@This CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 除去字符串头尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trim(@This CharSequence self, char c) {
        if (self == null) {
            return null;
        }

        // from String.trim()
        int len = self.length();
        int st = 0;

        while ((st < len) && (self.charAt(st) == c)) {
            st++;
        }
        while ((st < len) && (self.charAt(len - 1) == c)) {
            len--;
        }
        return ((st > 0) || (len < self.length())) ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串头部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimStart(@This CharSequence self, char c) {
        if (self == null) {
            return null;
        }

        int len = self.length();
        int st = 0;

        while ((st < len) && (self.charAt(st) == c)) {
            st++;
        }
        return st > 0 ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimEnd(@This CharSequence self, char c) {
        if (self == null) {
            return null;
        }

        int len = self.length();
        int st = 0;

        while ((st < len) && (self.charAt(len - 1) == c)) {
            len--;
        }
        return len < self.length() ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串头尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     * @see #trim(CharSequence, char[], boolean)
     */
    public static String trim(@This CharSequence self, char[] c) {
        return self.trim(c, true);
    }

    /**
     * 除去字符串头尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param sort  对c进行排序
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trim(@This CharSequence self, char[] c, boolean sort) {
        if (self == null) {
            return null;
        }

        // from String.trim()
        int len = self.length();
        int st = 0;

        if (sort) {
            Arrays.sort(c);
        }

        while ((st < len) && (Arrays.binarySearch(c, self.charAt(st)) >= 0)) {
            st++;
        }
        while ((st < len) && (Arrays.binarySearch(c, self.charAt(len - 1)) >= 0)) {
            len--;
        }
        return ((st > 0) || (len < self.length())) ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串头部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     * @see #trimStart(CharSequence, char[], boolean)
     */
    public static String trimStart(@This CharSequence self, char[] c) {
        return self.trimStart(c, true);
    }

    /**
     * 除去字符串头部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param sort  对c进行排序
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimStart(@This CharSequence self, char[] c, boolean sort) {
        if (self == null) {
            return null;
        }

        int len = self.length();
        int st = 0;

        if (sort) {
            Arrays.sort(c);
        }

        while ((st < len) && (Arrays.binarySearch(c, self.charAt(st)) >= 0)) {
            st++;
        }
        return st > 0 ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     * @see #trimEnd(CharSequence, char[], boolean)
     */
    public static String trimEnd(@This CharSequence self, char[] c) {
        return self.trimEnd(c, true);
    }

    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param sort  对c进行排序
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimEnd(@This CharSequence self, char[] c, boolean sort) {
        if (self == null) {
            return null;
        }

        if (c.isNullOrEmpty()) {
            return self.toString();
        }

        int len = self.length();
        int st = 0;

        if (sort) {
            Arrays.sort(c);
        }

        while ((st < len) && (Arrays.binarySearch(c, self.charAt(len - 1)) >= 0)) {
            len--;
        }
        return len < self.length() ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串头尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param cs    要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trim(@This CharSequence self, char c, char... cs) {
        if (cs.isNullOrEmpty()) {
            return self.trim(c);
        }

        return self.trim(cs.combine(c), true);
    }

    /**
     * 除去字符串头部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param cs    要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimStart(@This CharSequence self, char c, char... cs) {
        if (cs.isNullOrEmpty()) {
            return self.trimStart(c);
        }

        return self.trimStart(cs.combine(c), true);
    }

    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param c     要裁剪的字符
     * @param cs    要裁剪的字符
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimEnd(@This CharSequence self, char c, char... cs) {
        if (cs.isNullOrEmpty()) {
            return self.trimEnd(c);
        }

        return self.trimEnd(cs.combine(c), true);
    }

    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param cs    要裁剪的字符串
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trim(@This CharSequence self, CharSequence cs) {
        if (self == null) {
            return null;
        }

        if (cs.isNullOrEmpty()) {
            return self.toString();
        }

        int len = self.length();
        int st = 0;
        int clen = cs.length();
        int ct = 0;

        while ((st < len) && (self.charAt(st) == cs.charAt(ct))) {
            st++;
            ct++;
            if (ct == clen) {
                ct = 0;
            }
        }

        ct = clen;
        while ((st < len) && (self.charAt(len-1) == cs.charAt(clen-1))) {
            len--;
            ct--;
            if (ct == 0) {
                ct = clen;
            }
        }

        return ((st > 0) || (len < self.length())) ? self.subSequence(st, len).toString() : self.toString();
    }

    /**
     * 除去字符串头部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param cs    要裁剪的字符串
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimStart(@This CharSequence self, CharSequence cs) {
        if (self == null) {
            return null;
        }

        if (cs.isNullOrEmpty()) {
            return self.toString();
        }

        int len = self.length();
        int st = 0;
        int clen = cs.length();
        int ct = 0;

        while ((st < len) && (self.charAt(st) == cs.charAt(ct))) {
            st++;
            ct++;
            if (ct == clen) {
                ct = 0;
            }
        }
        return st > 0 ? self.subSequence(st, len).toString() : self.toString();
    }


    /**
     * 除去字符串尾部的字符，如果字符串是{@code null}，依然返回{@code null}。
     *
     * @param self  要处理的字符串
     * @param cs    要裁剪的字符串
     * @return 除去指定字符后的的字符串，如果原字串为{@code null}，则返回{@code null}
     */
    public static String trimEnd(@This CharSequence self, CharSequence cs) {
        if (self == null) {
            return null;
        }

        if (cs.isNullOrEmpty()) {
            return self.toString();
        }

        int len = self.length();
        int st = 0;
        int clen = cs.length();
        int ct = clen;

        while ((st < len) && (self.charAt(len-1) == cs.charAt(clen-1))) {
            len--;
            ct--;
            if (ct == 0) {
                ct = clen;
            }
        }

        return len < self.length() ? self.subSequence(st, len).toString() : self.toString();
    }
}
