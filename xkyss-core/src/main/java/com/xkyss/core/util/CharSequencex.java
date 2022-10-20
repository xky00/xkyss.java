package com.xkyss.core.util;

import com.xkyss.core.constant.StringPool;
import com.xkyss.core.function.ToBooleanBiFunction;

@SuppressWarnings({"unused", "SameParameterValue"})
public class CharSequencex implements StringPool {

    private static final int NOT_FOUND = -1;
    static final int TO_STRING_LIMIT = 16;

    private static boolean checkLaterThan1(final CharSequence cs, final CharSequence searchChar, final int len2, final int start1) {
        for (int i = 1, j = len2 - 1; i <= j; i++, j--) {
            if (cs.charAt(start1 + i) != searchChar.charAt(i) || cs.charAt(start1 + j) != searchChar.charAt(j)) {
                return false;
            }
        }
        return true;
    }

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
     * @param cs 字符串
     * @return true:是null或空字符串
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否全部是大写
     * @param cs 字符串
     * @return true-全部大写
     */
    public static boolean isAllUpperCase(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否全部小写
     * @param cs 字符串
     * @return true-全部小写
     */
    public static boolean isAllLowerCase(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

    public static char[] toCharArray(final CharSequence source) {
        final int len = Stringx.length(source);
        if (len == 0) {
            return Arrayx.EMPTY_CHAR_ARRAY;
        }
        if (source instanceof String) {
            return ((String) source).toCharArray();
        }
        final char[] array = new char[len];
        for (int i = 0; i < len; i++) {
            array[i] = source.charAt(i);
        }
        return array;
    }

    /**
     * 查找{@code searchChar}在{@code cs}中的索引号
     * @param cs 源字符串
     * @param searchChar 被搜索的字符
     * @param start 起始位置
     * @return 索引号
     */
    static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar.toString(), start);
        }
        if (cs instanceof StringBuilder) {
            return ((StringBuilder) cs).indexOf(searchChar.toString(), start);
        }
        if (cs instanceof StringBuffer) {
            return ((StringBuffer) cs).indexOf(searchChar.toString(), start);
        }
        return cs.toString().indexOf(searchChar.toString(), start);
    }

    /**
     * 查找{@code searchChar}在{@code cs}中的索引号
     * @param cs 源字符串
     * @param searchChar 被搜索的字符
     * @param start 起始位置
     * @return 索引号
     */
    static int indexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            start = 0;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i < sz; i++) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
            return NOT_FOUND;
        }
        //supplementary characters (LANG1300)
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            for (int i = start; i < sz - 1; i++) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (high == chars[0] && low == chars[1]) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    public static int indexOfAnyBut(final CharSequence cs, final char... searchChars) {
        if (isEmpty(cs) || Arrayx.isEmpty(searchChars)) {
            return NOT_FOUND;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        outer:
        for (int i = 0; i < csLen; i++) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch) {
                    if (i >= csLast || j >= searchLast || !Character.isHighSurrogate(ch)) {
                        continue outer;
                    }
                    if (searchChars[j + 1] == cs.charAt(i + 1)) {
                        continue outer;
                    }
                }
            }
            return i;
        }
        return NOT_FOUND;
    }

    /**
     * 查找{@code searchChar}在{@code cs}中的最后索引号
     * @param cs 源字符串
     * @param searchChar 被搜索的字符
     * @param start 起始位置
     * @return 索引号
     */
    static int lastIndexOf(final CharSequence cs, final CharSequence searchChar, int start) {
        if (searchChar == null || cs == null) {
            return NOT_FOUND;
        }
        if (searchChar instanceof String) {
            if (cs instanceof String) {
                return ((String) cs).lastIndexOf((String) searchChar, start);
            }
            if (cs instanceof StringBuilder) {
                return ((StringBuilder) cs).lastIndexOf((String) searchChar, start);
            }
            if (cs instanceof StringBuffer) {
                return ((StringBuffer) cs).lastIndexOf((String) searchChar, start);
            }
        }

        final int len1 = cs.length();
        final int len2 = searchChar.length();

        if (start > len1) {
            start = len1;
        }

        if (start < 0 || len2 > len1) {
            return NOT_FOUND;
        }

        if (len2 == 0) {
            return start;
        }

        if (len2 <= TO_STRING_LIMIT) {
            if (cs instanceof String) {
                return ((String) cs).lastIndexOf(searchChar.toString(), start);
            }
            if (cs instanceof StringBuilder) {
                return ((StringBuilder) cs).lastIndexOf(searchChar.toString(), start);
            }
            if (cs instanceof StringBuffer) {
                return ((StringBuffer) cs).lastIndexOf(searchChar.toString(), start);
            }
        }

        if (start + len2 > len1) {
            start = len1 - len2;
        }

        final char char0 = searchChar.charAt(0);

        int i = start;
        while (true) {
            while (cs.charAt(i) != char0) {
                i--;
                if (i < 0) {
                    return NOT_FOUND;
                }
            }
            if (checkLaterThan1(cs, searchChar, len2, i)) {
                return i;
            }
            i--;
            if (i < 0) {
                return NOT_FOUND;
            }
        }
    }

    /**
     * 查找{@code searchChar}在{@code cs}中的最后索引号
     * @param cs 源字符串
     * @param searchChar 被搜索的字符
     * @param start 起始位置
     * @return 索引号
     */
    static int lastIndexOf(final CharSequence cs, final int searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).lastIndexOf(searchChar, start);
        }
        final int sz = cs.length();
        if (start < 0) {
            return NOT_FOUND;
        }
        if (start >= sz) {
            start = sz - 1;
        }
        if (searchChar < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            for (int i = start; i >= 0; --i) {
                if (cs.charAt(i) == searchChar) {
                    return i;
                }
            }
            return NOT_FOUND;
        }
        //supplementary characters (LANG1300)
        //NOTE - we must do a forward traversal for this to avoid duplicating code points
        if (searchChar <= Character.MAX_CODE_POINT) {
            final char[] chars = Character.toChars(searchChar);
            //make sure it's not the last index
            if (start == sz - 1) {
                return NOT_FOUND;
            }
            for (int i = start; i >= 0; i--) {
                final char high = cs.charAt(i);
                final char low = cs.charAt(i + 1);
                if (chars[0] == high && chars[1] == low) {
                    return i;
                }
            }
        }
        return NOT_FOUND;
    }

    /**
     * 检查{@code seq}中是否包含{@code searchSeq}字符
     *
     * <pre>
     * contains(null, *)     = false
     * contains(*, null)     = false
     * contains("", "")      = true
     * contains("abc", "")   = true
     * contains("abc", "a")  = true
     * contains("abc", "z")  = false
     * </pre>
     *
     * @param seq 源字符串
     * @param searchSeq 被搜索的字符
     * @return true-包含
     */
    public static boolean contains(final CharSequence seq, final CharSequence searchSeq) {
        if (seq == null || searchSeq == null) {
            return false;
        }
        return indexOf(seq, searchSeq, 0) >= 0;
    }

    /**
     * 检查{@code seq}中是否包含{@code searchSeq}字符
     *
     * <pre>
     * StringUtils.contains(null, *)    = false
     * StringUtils.contains("", *)      = false
     * StringUtils.contains("abc", 'a') = true
     * StringUtils.contains("abc", 'z') = false
     * </pre>
     *
     * @param seq 源字符串
     * @param searchChar 被搜索的字符
     * @return true-包含
     */
    public static boolean contains(final CharSequence seq, final int searchChar) {
        if (isEmpty(seq)) {
            return false;
        }
        return indexOf(seq, searchChar, 0) >= 0;
    }

    /**
     *
     *
     * <pre>
     * containsAny(null, *)                  = false
     * containsAny("", *)                    = false
     * containsAny(*, null)                  = false
     * containsAny(*, [])                    = false
     * containsAny("zzabyycdxx", ['z', 'a']) = true
     * containsAny("zzabyycdxx", ['b', 'y']) = true
     * containsAny("zzabyycdxx", ['z', 'y']) = true
     * containsAny("aba", ['z'])             = false
     * </pre>
     *
     * @param cs 源字符串
     * @param searchChars 被搜索的字符组
     * @return {@code true} 包含任意被搜索字符,
     *         {@code false} 不包含, 或 {@code cs == null}
     */
    public static boolean containsAny(final CharSequence cs, final char... searchChars) {
        if (isEmpty(cs) || Arrayx.isEmpty(searchChars)) {
            return false;
        }
        final int csLength = cs.length();
        final int searchLength = searchChars.length;
        final int csLast = csLength - 1;
        final int searchLast = searchLength - 1;
        for (int i = 0; i < csLength; i++) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch)) {
                        // ch is in the Basic Multilingual Plane
                        return true;
                    }
                    if (j == searchLast) {
                        // missing low surrogate, fine, like String.indexOf(String)
                        return true;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <pre>
     * containsAny(null, *)               = false
     * containsAny("", *)                 = false
     * containsAny(*, null)               = false
     * containsAny(*, "")                 = false
     * containsAny("zzabyycdxx", "za")    = true
     * containsAny("zzabyycdxx", "by")    = true
     * containsAny("zzabyycdxx", "zy")    = true
     * containsAny("zzabyycdxx", "\tx")   = true
     * containsAny("zzabyycdxx", "$.#yF") = true
     * containsAny("aba", "z")            = false
     * </pre>
     *
     */
    public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
        if (searchChars == null) {
            return false;
        }
        return containsAny(cs, toCharArray(searchChars));
    }

    /**
     * <pre>
     * StringUtils.containsAny(null, *)            = false
     * StringUtils.containsAny("", *)              = false
     * StringUtils.containsAny(*, null)            = false
     * StringUtils.containsAny(*, [])              = false
     * StringUtils.containsAny("abcd", "ab", null) = true
     * StringUtils.containsAny("abcd", "ab", "cd") = true
     * StringUtils.containsAny("abc", "d", "abc")  = true
     * </pre>
     */
    public static boolean containsAny(final CharSequence cs, final CharSequence... searchCharSequences) {
        return containsAny(CharSequencex::contains, cs, searchCharSequences);
    }

    /**
     * Checks if the CharSequence contains any of the CharSequences in the given array.
     *
     * <p>
     * A {@code null} {@code cs} CharSequence will return {@code false}. A {@code null} or zero length search array will
     * return {@code false}.
     * </p>
     *
     * @param cs The CharSequence to check, may be null
     * @param searchCharSequences The array of CharSequences to search for, may be null. Individual CharSequences may be
     *        null as well.
     * @return {@code true} if any of the search CharSequences are found, {@code false} otherwise
     * @since 3.12.0
     */
    private static boolean containsAny(final ToBooleanBiFunction<CharSequence, CharSequence> test,
                                       final CharSequence cs, final CharSequence... searchCharSequences) {
        if (isEmpty(cs) || Arrayx.isEmpty(searchCharSequences)) {
            return false;
        }
        for (final CharSequence searchCharSequence : searchCharSequences) {
            if (test.applyAsBoolean(cs, searchCharSequence)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAnyIgnoreCase(final CharSequence cs, final CharSequence... searchCharSequences) {
        return containsAny(CharSequencex::containsIgnoreCase, cs, searchCharSequences);
    }

    public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsNone(final CharSequence cs, final char... searchChars) {
        if (cs == null || searchChars == null) {
            return true;
        }
        final int csLen = cs.length();
        final int csLast = csLen - 1;
        final int searchLen = searchChars.length;
        final int searchLast = searchLen - 1;
        for (int i = 0; i < csLen; i++) {
            final char ch = cs.charAt(i);
            for (int j = 0; j < searchLen; j++) {
                if (searchChars[j] == ch) {
                    if (!Character.isHighSurrogate(ch)) {
                        // ch is in the Basic Multilingual Plane
                        return false;
                    }
                    if (j == searchLast) {
                        // missing low surrogate, fine, like String.indexOf(String)
                        return false;
                    }
                    if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean containsNone(final CharSequence cs, final String invalidChars) {
        if (invalidChars == null) {
            return true;
        }
        return containsNone(cs, invalidChars.toCharArray());
    }

    public static boolean containsOnly(final CharSequence cs, final char... valid) {
        // All these pre-checks are to maintain API with an older version
        if (valid == null || cs == null) {
            return false;
        }
        if (cs.length() == 0) {
            return true;
        }
        if (valid.length == 0) {
            return false;
        }
        return indexOfAnyBut(cs, valid) == NOT_FOUND;
    }

    public static boolean containsOnly(final CharSequence cs, final String validChars) {
        if (cs == null || validChars == null) {
            return false;
        }
        return containsOnly(cs, validChars.toCharArray());
    }

    public static boolean containsWhitespace(final CharSequence seq) {
        if (isEmpty(seq)) {
            return false;
        }
        final int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

}
