package com.xkyss.core.util;


/**
 * [CharSequence]工具类
 *
 * @ref hutool.cn
 *
 * @author xkyii
 * @createdAt 2021/07/28.
 */
public class CharSequenceUtil {

    /**
     * <p>指定字符串数组中，是否包含空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.hasBlank()                  // true}</li>
     *     <li>{@code StrUtil.hasBlank("", null, " ")     // true}</li>
     *     <li>{@code StrUtil.hasBlank("123", " ")        // true}</li>
     *     <li>{@code StrUtil.hasBlank("123", "abc")      // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isAllBlank(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>hasBlank(CharSequence...)            等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
     *     <li>{@link #isAllBlank(CharSequence...)} 等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

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
    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (!CharUtil.isBlankChar(str.charAt(i))) {
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
     * @param str 被检测的字符串
     * @return 是否为非空
     * @see #isBlank(CharSequence)
     */
    public static boolean isNotBlank(CharSequence str) {
        return false == isBlank(str);
    }

    /**
     * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isAllBlank()                  // true}</li>
     *     <li>{@code StrUtil.isAllBlank("", null, " ")     // true}</li>
     *     <li>{@code StrUtil.isAllBlank("123", " ")        // false}</li>
     *     <li>{@code StrUtil.isAllBlank("123", "abc")      // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #hasBlank(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>{@link #hasBlank(CharSequence...)}   等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
     *     <li>isAllBlank(CharSequence...)          等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 所有字符串是否为空白
     */
    public static boolean isAllBlank(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
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
    public static boolean isEmpty(CharSequence str) {
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
    public static boolean isNotEmpty(CharSequence str) {
        return false == isEmpty(str);
    }

    /**
     * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.isAllEmpty()                  // true}</li>
     *     <li>{@code StrUtil.isAllEmpty("", null)          // true}</li>
     *     <li>{@code StrUtil.isAllEmpty("123", "")         // false}</li>
     *     <li>{@code StrUtil.isAllEmpty("123", "abc")      // false}</li>
     *     <li>{@code StrUtil.isAllEmpty(" ", "\t", "\n")   // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #hasEmpty(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>{@link #hasEmpty(CharSequence...)}   等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
     *     <li>isAllEmpty(CharSequence...)          等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 所有字符串是否为空白
     */
    public static boolean isAllEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>是否包含空字符串。</p>
     * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
     * <br>
     *
     * <p>例：</p>
     * <ul>
     *     <li>{@code StrUtil.hasEmpty()                  // true}</li>
     *     <li>{@code StrUtil.hasEmpty("", null)          // true}</li>
     *     <li>{@code StrUtil.hasEmpty("123", "")         // true}</li>
     *     <li>{@code StrUtil.hasEmpty("123", "abc")      // false}</li>
     *     <li>{@code StrUtil.hasEmpty(" ", "\t", "\n")   // false}</li>
     * </ul>
     *
     * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
     * <ul>
     *     <li>hasEmpty(CharSequence...)            等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
     *     <li>{@link #isAllEmpty(CharSequence...)} 等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
     * </ul>
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasEmpty(CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    // #region startWith

    /**
     * 字符串是否以给定字符开始
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否开始
     */
    public static boolean startWith(CharSequence str, char c) {
        if (isEmpty(str)) {
            return false;
        }
        return c == str.charAt(0);
    }
    /**
     * 是否以指定字符串开头<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str        被监测字符串
     * @param prefix     开头字符串
     * @param ignoreCase 是否忽略大小写
     * @return 是否以指定字符串开头
     * @since 5.4.3
     */
    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return startWith(str, prefix, ignoreCase, false);
    }
    /**
     * 是否以指定字符串开头<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str          被监测字符串
     * @param prefix       开头字符串
     * @param ignoreCase   是否忽略大小写
     * @param ignoreEquals 是否忽略字符串相等的情况
     * @return 是否以指定字符串开头
     * @since 5.4.3
     */
    public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
        if (null == str || null == prefix) {
            if (!ignoreEquals) {
                return false;
            }
            return null == str && null == prefix;
        }

        boolean isStartWith;
        if (ignoreCase) {
            isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
        } else {
            isStartWith = str.toString().startsWith(prefix.toString());
        }

        if (isStartWith) {
            return (!ignoreEquals) || (false == equals(str, prefix, ignoreCase));
        }
        return false;
    }

    /**
     * 是否以指定字符串开头
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWith(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false);
    }

    /**
     * 是否以指定字符串开头，忽略相等字符串的情况
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头并且两个字符串不相等
     */
    public static boolean startWithIgnoreEquals(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, false, true);
    }

    /**
     * 是否以指定字符串开头，忽略大小写
     *
     * @param str    被监测字符串
     * @param prefix 开头字符串
     * @return 是否以指定字符串开头
     */
    public static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startWith(str, prefix, true);
    }

    /**
     * 给定字符串是否以任何一个字符串开始<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @return 给定字符串是否以任何一个字符串开始
     * @since 3.0.6
     */
    public static boolean startWithAny(CharSequence str, CharSequence... prefixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    // #endregion startWith

    // #region endWith


    /**
     * 字符串是否以给定字符结尾
     *
     * @param str 字符串
     * @param c   字符
     * @return 是否结尾
     */
    public static boolean endWith(CharSequence str, char c) {
        if (isEmpty(str)) {
            return false;
        }
        return c == str.charAt(str.length() - 1);
    }

    /**
     * 是否以指定字符串结尾<br>
     * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
     *
     * @param str          被监测字符串
     * @param suffix       结尾字符串
     * @param isIgnoreCase 是否忽略大小写
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
        if (null == str || null == suffix) {
            return null == str && null == suffix;
        }

        if (isIgnoreCase) {
            return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
        } else {
            return str.toString().endsWith(suffix.toString());
        }
    }

    /**
     * 是否以指定字符串结尾
     *
     * @param str    被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWith(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, false);
    }

    /**
     * 是否以指定字符串结尾，忽略大小写
     *
     * @param str    被监测字符串
     * @param suffix 结尾字符串
     * @return 是否以指定字符串结尾
     */
    public static boolean endWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endWith(str, suffix, true);
    }

    /**
     * 给定字符串是否以任何一个字符串结尾<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     * @since 3.0.6
     */
    public static boolean endWithAny(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
            return false;
        }

        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 给定字符串是否以任何一个字符串结尾（忽略大小写）<br>
     * 给定字符串和数组为空都返回false
     *
     * @param str      给定字符串
     * @param suffixes 需要检测的结尾字符串
     * @return 给定字符串是否以任何一个字符串结尾
     * @since 5.5.9
     */
    public static boolean endWithAnyIgnoreCase(CharSequence str, CharSequence... suffixes) {
        if (isEmpty(str) || ArrayUtil.isEmpty(suffixes)) {
            return false;
        }

        for (CharSequence suffix : suffixes) {
            if (endWith(str, suffix, true)) {
                return true;
            }
        }
        return false;
    }

    // #endregion startWith

    // #region equals

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * <pre>
     * equals(null, null)   = true
     * equals(null, &quot;abc&quot;)  = false
     * equals(&quot;abc&quot;, null)  = false
     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是{@code null}，则返回{@code true}
     * @since 3.2.0
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.toString().contentEquals(str2);
        }
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1 给定需要检查的字符串
     * @param strs 需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAnyIgnoreCase(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, true, strs);
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1 给定需要检查的字符串
     * @param strs 需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAny(CharSequence str1, CharSequence... strs) {
        return equalsAny(str1, false, strs);
    }

    /**
     * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
     * 如果参与比对的字符串列表为空，返回{@code false}
     *
     * @param str1       给定需要检查的字符串
     * @param ignoreCase 是否忽略大小写
     * @param strs       需要参与比对的字符串列表
     * @return 是否相同
     * @since 4.3.2
     */
    public static boolean equalsAny(CharSequence str1, boolean ignoreCase, CharSequence... strs) {
        if (ArrayUtil.isEmpty(strs)) {
            return false;
        }

        for (CharSequence str : strs) {
            if (equals(str1, str, ignoreCase)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 字符串指定位置的字符是否与给定字符相同<br>
     * 如果字符串为null，返回false<br>
     * 如果给定的位置大于字符串长度，返回false<br>
     * 如果给定的位置小于0，返回false
     *
     * @param str      字符串
     * @param position 位置
     * @param c        需要对比的字符
     * @return 字符串指定位置的字符是否与给定字符相同
     * @since 3.3.1
     */
    public static boolean equalsCharAt(CharSequence str, int position, char c) {
        if (null == str || position < 0) {
            return false;
        }
        return str.length() > position && c == str.charAt(position);
    }

    /**
     * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
     * 任意一个字符串为null返回false
     *
     * @param str1       第一个字符串
     * @param start1     第一个字符串开始的位置
     * @param str2       第二个字符串
     * @param start2     第二个字符串开始的位置
     * @param length     截取长度
     * @param ignoreCase 是否忽略大小写
     * @return 子串是否相同
     * @since 3.2.1
     */
    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        if (null == str1 || null == str2) {
            return false;
        }

        return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
    }

    // #endregion equals
}
