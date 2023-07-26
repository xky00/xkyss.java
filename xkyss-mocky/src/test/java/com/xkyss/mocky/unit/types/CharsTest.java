package com.xkyss.mocky.unit.types;

import com.xkyss.mocky.unit.objects.Froms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static com.xkyss.mocky.constants.MockTestConst.TEST_COUNT;
import static com.xkyss.mocky.contant.Alphabets.*;

public class CharsTest {

    Chars chars;

    @BeforeEach
    public void init() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        chars = new Chars(random, new Froms(random));
    }

    @Test
    public void testAlphaNumeric() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(ALPHA_NUMERIC.contains(chars.get()));
        }
    }

    @Test
    public void testDigits() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(DIGITS.contains(chars.digits().get()));
        }
    }

    @Test
    public void testLowerLetters() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(LETTERS_LOWERCASE.contains(chars.lowerLetter().get()));
        }
    }

    @Test
    public void testUpperLetters() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(LETTERS_UPPERCASE.contains(chars.upperLetter().get()));
        }
    }

    @Test
    public void testHexa() {
        for (int i = 0; i <TEST_COUNT; i++) {
            Assertions.assertTrue(HEXA.contains(chars.hex().get()));
        }
    }

    @Test
    public void testFromStringEmptyAlphabet() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> chars.from(""));
    }

    @Test
    public void testFromStringNullAlphabet() {
        Assertions.assertThrows(NullPointerException.class, () -> chars.from((String) null));
    }

    @Test
    public void testFromArrayEmpty() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> chars.from(new char[] {}));

    }

    @Test
    public void testFromArrayNull() {
        Assertions.assertThrows(NullPointerException.class, () -> chars.from((char[]) null));
    }

}
