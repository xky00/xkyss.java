package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.UnaryOperator;

public class HashesImpl implements Hashes {

    private static final Integer HASHED_STRING_SIZE = 128;

    private final Strings strings;
    private final Random random;

    public HashesImpl(Random random, Strings strings) {
        this.random = random;
        this.strings = strings;
    }

    @Override
    public String get() {
        List<MockUnit<String>> functions = Arrays.asList(
            this::md2,
            this::md5,
            this::sha1,
            this::sha256,
            this::sha384,
            this::sha512
        );
        int idx = random.nextInt(functions.size());
        return functions.get(idx).get();
    }

    private MockUnit<String> supplier(UnaryOperator<String> digester) {
        return strings.size(HASHED_STRING_SIZE).map(digester);
    }

    public String md2() {
        return supplier(DigestUtils::md2Hex).get();
    }

    public String md5() {
        return supplier(DigestUtils::md5Hex).get();
    }

    public String sha1() {
        return supplier(DigestUtils::sha1Hex).get();
    }

    public String sha256() {
        return supplier(DigestUtils::sha256Hex).get();
    }

    public String sha384() {
        return supplier(DigestUtils::sha384Hex).get();
    }

    public String sha512() {
        return supplier(DigestUtils::sha512Hex).get();
    }
}
