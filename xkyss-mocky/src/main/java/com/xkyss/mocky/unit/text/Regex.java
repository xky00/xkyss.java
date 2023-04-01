package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;

public interface Regex extends MockUnit<String> {
    static Regex defaultOf(String regExp) {
        return new RegexImpl(regExp);
    }
}
