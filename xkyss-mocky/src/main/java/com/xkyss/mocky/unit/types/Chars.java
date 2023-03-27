package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.contant.CharsType;
import com.xkyss.mocky.unit.objects.Froms;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Chars extends MockUnit<Character> {

    static Chars defaultWith(Random random, Froms froms) {
        return new CharsImpl(random, froms);
    }

    default MockUnit<Character> digits() {
        throw new NotImplementedException();
    }

    default MockUnit<Character> lowerLetter() {
        throw new NotImplementedException();
    }

    default MockUnit<Character> upperLetter() {
        throw new NotImplementedException();
    }

    default MockUnit<Character> letter() {
        throw new NotImplementedException();
    }

    default MockUnit<Character> hex() {
        throw new NotImplementedException();
    }

    default MockUnit<Character> from(String alphabet) {
        throw new NotImplementedException();
    }

    default MockUnit<Character> from(char[] alphabet) {
        throw new NotImplementedException();
    }

    default MockUnit<Character> type(CharsType type) {
        throw new NotImplementedException();
    }

    default MockUnit<Character> types(CharsType... types) {
        throw new NotImplementedException();
    }
}
