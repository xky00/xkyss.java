package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.abstraction.MockUnit;
import com.xkyss.mocky.contant.CharsType;
import com.xkyss.mocky.unit.objects.Froms;

import java.util.Random;

import static com.xkyss.core.util.Validate.*;
import static com.xkyss.mocky.contant.Alphabets.*;


public class Chars implements MockUnit<Character> {

    private final Random random;

    private final Froms froms;


    public Chars(Random random, Froms froms) {
        this.random = random;
        this.froms = froms;
    }

    @Override
    public Character get() {
        return froms.from(ALPHA_NUMERIC).get();
    }
    
    public MockUnit<Character> digits() {
        return froms.from(DIGITS);
    }

    public MockUnit<Character> lowerLetter() {
        return froms.from(LETTERS_LOWERCASE);
    }

    public MockUnit<Character> upperLetter() {
        return froms.from(LETTERS_UPPERCASE);
    }

    public MockUnit<Character> letter() {
        return froms.from(LETTERS);
    }

    public MockUnit<Character> hex() {
        return froms.from(HEXA);
    }

    public MockUnit<Character> from(String alphabet) {
        notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length());
            return alphabet.charAt(idx);
        };
    }

    public MockUnit<Character> from(char[] alphabet) {
        notEmpty(alphabet, "alphabet");
        return () -> {
            int idx = random.nextInt(alphabet.length);
            return alphabet[idx];
        };
    }

    public MockUnit<Character> type(CharsType type) {
        notNull(type, "type");

        switch (type) {
            case DIGITS: return digits();
            case HEX: return hex();
            case LOWER_LETTERS: return lowerLetter();
            case UPPER_LETTERS: return upperLetter();
            case LETTERS: return letter();
            case ALPHA_NUMERIC: return this;
            default: throw new IllegalArgumentException("Invalid CharsType");
        }
    }

    public MockUnit<Character> types(CharsType... types) {
        notEmptyOrNullValues(types, "types");

        CharsType type = froms.from(types).get();
        return type(type);
    }
}
