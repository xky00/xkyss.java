package com.xkyss.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    public void test_01() {
        Pattern pattern = Pattern.compile("/mlcache/cache/([^/]+/)*");
        Matcher matcher = pattern.matcher("/mlcache/cache/a/b/3/");
        boolean matches = matcher.matches();

        String matchedText = matcher.group(); // "/mlcache/cache/1/2/3"
        String c1 = matcher.group(1); // "1/2/3"
        String c2 = matcher.group(2); // "1/2/3"
        String c3 = matcher.group(3); // "1/2/3"
    }

    @Test
    public void test_02() {
        String cacheKey = "/mlcache/cache/a/b/c";
        String[] matcher = cacheKey.split("/");

        Assertions.assertEquals(6, matcher.length);
    }
}
