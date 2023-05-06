package com.xkyss.core.naming;

import com.xkyss.core.util.Collectorx;
import com.xkyss.core.util.Stringx;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 命名转换器
 * @author xkyii
 * @date 2022-10-20
 * @see "https://github.com/jawira/case-converter"
 */
@SuppressWarnings("ALL")
public class Converter {

    public static Converter EMPTY = new Converter();

    public static final char SEPARATOR_UNDERSCORE = '_';
    public static final char SEPARATOR_DASH = '-';
    public static final char SEPARATOR_DOT = '.';
    public static final char SEPARATOR_SPACE = ' ';

    /**
     * 原始字符串
     */
    private String original = "";

    private String[] words = {};

    public static Converter fromAuto(String original) {
        Converter c = new Converter();
        c.original = original;
        if (StringUtils.isEmpty(c.original)) {
            return c;
        }

        if (StringUtils.contains(c.original, SEPARATOR_UNDERSCORE)) {
            c.words = StringUtils.split(c.original, SEPARATOR_UNDERSCORE);
        }
        else if (StringUtils.contains(c.original, SEPARATOR_DASH)) {
            c.words = StringUtils.split(c.original, SEPARATOR_DASH);
        }
        else if (StringUtils.contains(c.original, SEPARATOR_DOT)) {
            c.words = StringUtils.split(c.original, SEPARATOR_DOT);
        }
        else if (StringUtils.contains(c.original, SEPARATOR_SPACE)) {
            c.words = StringUtils.split(c.original, SEPARATOR_SPACE);
        }
        else if (StringUtils.isAllUpperCase(c.original)) {
            c.words = Stringx.splitByLength(c.original, 1);
        }
        else {
            c.words = StringUtils.splitByCharacterTypeCamelCase(c.original);
        }

        return c;
    }

    private static Converter from(String original, char separator) {
        Converter c = new Converter();
        c.original = original;
        if (StringUtils.isEmpty(c.original)) {
            return c;
        }

        c.words = StringUtils.split(c.original, separator);
        return c;
    }

    public static Converter fromAda(String original) {
        return from(original, SEPARATOR_UNDERSCORE);
    }

    public static Converter fromSnake(String original) {
        return from(original, SEPARATOR_UNDERSCORE);
    }

    public static Converter fromMacro(String original) {
        return from(original, SEPARATOR_UNDERSCORE);
    }

    public static Converter fromCamel(String original) {
        Converter c = new Converter();
        c.original = original;
        if (StringUtils.isEmpty(c.original)) {
            return c;
        }

        c.words = StringUtils.splitByCharacterTypeCamelCase(c.original);
        return c;
    }

    public static Converter fromPascal(String original) {
        Converter c = new Converter();
        c.original = original;
        if (StringUtils.isEmpty(c.original)) {
            return c;
        }

        c.words = StringUtils.splitByCharacterTypeCamelCase(c.original);
        return c;
    }

    public static Converter fromKebab(String original) {
        return from(original, SEPARATOR_DASH);
    }

    public static Converter fromTrain(String original) {
        return from(original, SEPARATOR_DASH);
    }

    public static Converter fromCobol(String original) {
        return from(original, SEPARATOR_DASH);
    }

    public static Converter fromLower(String original) {
        return from(original, SEPARATOR_SPACE);
    }

    public static Converter fromUpper(String original) {
        return from(original, SEPARATOR_SPACE);
    }

    public static Converter fromTitle(String original) {
        return from(original, SEPARATOR_SPACE);
    }

    public static Converter fromSentence(String original) {
        return from(original, SEPARATOR_SPACE);
    }

    public static Converter fromDot(String original) {
        return from(original, SEPARATOR_DOT);
    }

    public String getOriginal() {
        return this.original;
    }

    public String[] getWords() {
        return this.words;
    }

    /**
     * 首字母大写, {code '_'}连接
     * <pre>
     *     Ada_Case_Name
     * </pre>
     */
    public String toAda() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .map(StringUtils::capitalize)
            .collect(Collectorx.joining(SEPARATOR_UNDERSCORE));
    }

    /**
     * 第一个单次首字母小写, 其他首字母大写
     * <pre>
     *     camelCaseName
     * </pre>
     */
    public String toCamel() {
        return IntStream.range(0, this.words.length)
            .mapToObj(i -> (i == 0)
                ? StringUtils.toRootLowerCase(this.words[i])
                : StringUtils.capitalize(StringUtils.toRootLowerCase(this.words[i])))
            .collect(Collectors.joining());
    }

    /**
     * 全大写, {code '-'}连接
     * <pre>
     *     COBEL-CASE-NAME
     * </pre>
     */
    public String toCobol() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootUpperCase)
            .collect(Collectorx.joining(SEPARATOR_DASH));
    }

    /**
     * 全小写, {code '_'}连接
     * <pre>
     *     lower_case_name
     * </pre>
     */
    public String toMacro() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootUpperCase)
            .collect(Collectorx.joining(SEPARATOR_UNDERSCORE));
    }

    /**
     * 全小写, {code '.'}连接
     * <pre>
     *     dot.case.name
     * </pre>
     */
    public String toDot() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .collect(Collectorx.joining(SEPARATOR_DOT));
    }

    /**
     * 全小写, {code '-'}连接
     * <pre>
     *     dot-case-name
     * </pre>
     */
    public String toKebab() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .collect(Collectorx.joining(SEPARATOR_DASH));
    }

    /**
     * 全小写, {code ' '}连接
     * <pre>
     *     lower case name
     * </pre>
     */
    public String toLower() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .collect(Collectorx.joining(SEPARATOR_SPACE));
    }

    /**
     * 首字母大写
     * <pre>
     *     CamelCaseName
     * </pre>
     */
    public String toPascal() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .map(StringUtils::capitalize)
            .collect(Collectors.joining());
    }

    /**
     * 第一个单词首字母大写, 其余单词小写, ' '连接
     * <pre>
     *     Sentence case name
     * </pre>
     */
    public String toSentence() {
        return IntStream.range(0, this.words.length)
            .mapToObj(i -> (i == 0)
                ? StringUtils.capitalize(StringUtils.toRootLowerCase(this.words[i]))
                : StringUtils.toRootLowerCase(this.words[i]))
            .collect(Collectorx.joining(SEPARATOR_SPACE));
    }

    /**
     * 全小写, '_'连接
     * <pre>
     *     snake_case_name
     * </pre>
     */
    public String toSnake() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .collect(Collectorx.joining(SEPARATOR_UNDERSCORE));
    }

    /**
     * 全部单词首字母大写, {@code ' '}连接
     * <pre>
     *     Title Case Name
     * </pre>
     */
    public String toTitle() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .map(StringUtils::capitalize)
            .collect(Collectorx.joining(SEPARATOR_SPACE));
    }

    /**
     * 全部单词首字母大写, {@code '-'}连接
     * <pre>
     *     Title-Case-Name
     * </pre>
     */
    public String toTrain() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootLowerCase)
            .map(StringUtils::capitalize)
            .collect(Collectorx.joining(SEPARATOR_DASH));
    }

    /**
     * 全大写, {@code ' '}连接
     * <pre>
     *     UPPER CASE NAME
     * </pre>
     */
    public String toUpper() {
        return Arrays.stream(this.words)
            .map(StringUtils::toRootUpperCase)
            .collect(Collectorx.joining(SEPARATOR_SPACE));
    }
}
