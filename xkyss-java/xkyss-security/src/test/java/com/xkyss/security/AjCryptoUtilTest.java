package com.xkyss.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class AjCryptoUtilTest {
    @Test
    public void testMd5AndBase64() {
        String key = "DD8AA8E5AA5F4335A7ABD95671319231";
        String pwd = "123456";

        String md5b64 = AjCryptoUtil.md5AndBase64(key + pwd);
        Assertions.assertEquals("ycBQWaGsjCgNWQYy3MEJWg==", md5b64);
        String password = AjCryptoUtil.md5AndBase64(key + md5b64);
        Assertions.assertEquals("pH4ssWTAHjL8AgtROfoNWg==", password);
    }

    @Test
    public void testSm4() throws NoSuchAlgorithmException {

        String username = "test12";
        String base64md5 = "ycBQWaGsjCgNWQYy3MEJWg==";

        String encrypted = AjCryptoUtil.sm4Encrypt(username, base64md5);
        Assertions.assertEquals("+b/ZikCM+4D3urKAuuEk+phBVuitQx1zMMDY7OtV9zM=", encrypted);
    }
}
