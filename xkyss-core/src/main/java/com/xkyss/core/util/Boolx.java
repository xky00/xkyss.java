package com.xkyss.core.util;

public class Boolx {
    /**
     * The false String {@code "false"}.
     *
     * @since 3.12.0
     */
    public static final String FALSE = "false";

    /**
     * The no String {@code "no"}.
     *
     * @since 3.12.0
     */
    public static final String NO = "no";

    /**
     * The off String {@code "off"}.
     *
     * @since 3.12.0
     */
    public static final String OFF = "off";

    /**
     * The on String {@code "on"}.
     *
     * @since 3.12.0
     */
    public static final String ON = "on";

    /**
     * The true String {@code "true"}.
     *
     * @since 3.12.0
     */
    public static final String TRUE = "true";

    /**
     * The yes String {@code "yes"}.
     *
     * @since 3.12.0
     */
    public static final String YES = "yes";

    /**
     * Converts a boolean to a String returning one of the input Strings.
     *
     * <pre>
     *   BooleanUtils.toString(true, "true", "false")   = "true"
     *   BooleanUtils.toString(false, "true", "false")  = "false"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @param trueString  the String to return if {@code true}, may be {@code null}
     * @param falseString  the String to return if {@code false}, may be {@code null}
     * @return one of the two input Strings
     */
    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? trueString : falseString;
    }

    /**
     * Converts a Boolean to a String returning one of the input Strings.
     *
     * <pre>
     *   BooleanUtils.toString(Boolean.TRUE, "true", "false", null)   = "true"
     *   BooleanUtils.toString(Boolean.FALSE, "true", "false", null)  = "false"
     *   BooleanUtils.toString(null, "true", "false", null)           = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @param trueString  the String to return if {@code true}, may be {@code null}
     * @param falseString  the String to return if {@code false}, may be {@code null}
     * @param nullString  the String to return if {@code null}, may be {@code null}
     * @return one of the three input Strings
     */
    public static String toString(final Boolean bool, final String trueString, final String falseString, final String nullString) {
        if (bool == null) {
            return nullString;
        }
        return bool.booleanValue() ? trueString : falseString;
    }

    /**
     * Converts a boolean to a String returning {@code 'on'}
     * or {@code 'off'}.
     *
     * <pre>
     *   BooleanUtils.toStringOnOff(true)   = "on"
     *   BooleanUtils.toStringOnOff(false)  = "off"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'on'}, {@code 'off'}, or {@code null}
     */
    public static String toStringOnOff(final boolean bool) {
        return toString(bool, ON, OFF);
    }

    /**
     * Converts a Boolean to a String returning {@code 'on'},
     * {@code 'off'}, or {@code null}.
     *
     * <pre>
     *   BooleanUtils.toStringOnOff(Boolean.TRUE)  = "on"
     *   BooleanUtils.toStringOnOff(Boolean.FALSE) = "off"
     *   BooleanUtils.toStringOnOff(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'on'}, {@code 'off'}, or {@code null}
     */
    public static String toStringOnOff(final Boolean bool) {
        return toString(bool, ON, OFF, null);
    }

    /**
     * Converts a boolean to a String returning {@code 'true'}
     * or {@code 'false'}.
     *
     * <pre>
     *   BooleanUtils.toStringTrueFalse(true)   = "true"
     *   BooleanUtils.toStringTrueFalse(false)  = "false"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'true'}, {@code 'false'}, or {@code null}
     */
    public static String toStringTrueFalse(final boolean bool) {
        return toString(bool, TRUE, FALSE);
    }

    /**
     * Converts a Boolean to a String returning {@code 'true'},
     * {@code 'false'}, or {@code null}.
     *
     * <pre>
     *   BooleanUtils.toStringTrueFalse(Boolean.TRUE)  = "true"
     *   BooleanUtils.toStringTrueFalse(Boolean.FALSE) = "false"
     *   BooleanUtils.toStringTrueFalse(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'true'}, {@code 'false'}, or {@code null}
     */
    public static String toStringTrueFalse(final Boolean bool) {
        return toString(bool, TRUE, FALSE, null);
    }

    /**
     * Converts a boolean to a String returning {@code 'yes'}
     * or {@code 'no'}.
     *
     * <pre>
     *   BooleanUtils.toStringYesNo(true)   = "yes"
     *   BooleanUtils.toStringYesNo(false)  = "no"
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'yes'}, {@code 'no'}, or {@code null}
     */
    public static String toStringYesNo(final boolean bool) {
        return toString(bool, YES, NO);
    }

    /**
     * Converts a Boolean to a String returning {@code 'yes'},
     * {@code 'no'}, or {@code null}.
     *
     * <pre>
     *   BooleanUtils.toStringYesNo(Boolean.TRUE)  = "yes"
     *   BooleanUtils.toStringYesNo(Boolean.FALSE) = "no"
     *   BooleanUtils.toStringYesNo(null)          = null;
     * </pre>
     *
     * @param bool  the Boolean to check
     * @return {@code 'yes'}, {@code 'no'}, or {@code null}
     */
    public static String toStringYesNo(final Boolean bool) {
        return toString(bool, YES, NO, null);
    }
}
