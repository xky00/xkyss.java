package com.xkyss.mocky.unit.text;

import com.mifmif.common.regex.Generex;
import com.xkyss.mocky.abstraction.MockUnit;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.xkyss.mocky.contant.MockConsts.INVALID_REGEX_PATTERN;
import static org.apache.commons.lang3.Validate.notNull;

public class Regex implements MockUnit<String> {

    private final String regExp;

    public Regex(String regExp) {
        this.regExp = regExp;
    }

    @Override
    public String get() {
        notNull(regExp, "regExp");
        validRegex(regExp);
        return new Generex(regExp).random();
    }

    private static void validRegex(String regex) {
        try {
            Pattern.compile(regex);
        } catch (PatternSyntaxException pse) {
            String fmt = String.format(INVALID_REGEX_PATTERN, regex);
            throw new IllegalArgumentException(fmt, pse);
        }
    }
}
