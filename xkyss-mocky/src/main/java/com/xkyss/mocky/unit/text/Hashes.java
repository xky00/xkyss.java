package com.xkyss.mocky.unit.text;

import com.xkyss.mocky.abstraction.MockUnit;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Random;

public interface Hashes extends MockUnit<String> {

    static Hashes defaultOf(Random random, Strings strings) {
        return new HashesImpl(random, strings);
    }

    default String md2() {
        throw new NotImplementedException();
    }

    default String md5() {
        throw new NotImplementedException();
    }

    default String sha1() {
        throw new NotImplementedException();
    }

    default String sha256() {
        throw new NotImplementedException();
    }

    default String sha384() {
        throw new NotImplementedException();
    }

    default String sha512() {
        throw new NotImplementedException();
    }
}
