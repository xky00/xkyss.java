package com.xkyss.security;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordTest {
    @Test
    public void testMd5AndBase64() {
        String key = "DD8AA8E5AA5F4335A7ABD95671319231";
        String pwd = "123456";

        byte[] digest = SecureUtil.md5().digest(key + pwd);

        String encoded = Base64.getEncoder().encodeToString(digest);
//        String encoded = md5AndBase64(pwd, key, 1);
        Assertions.assertEquals("ycBQWaGsjCgNWQYy3MEJWg==", encoded);
    }

    @Test
    public void testSm4() throws NoSuchAlgorithmException {

        String username = "test12";
        String base64md5 = "ycBQWaGsjCgNWQYy3MEJWg==";

        String encrypted = encrypt(username, base64md5);
        Assertions.assertEquals("+b/ZikCM+4D3urKAuuEk+phBVuitQx1zMMDY7OtV9zM=", encrypted);
    }

    private String encrypt(String xorKey, String data) throws NoSuchAlgorithmException {
        // md5
        byte[] kbytes = SecureUtil.md5().digest(xorKey);
        // xor
        byte[] xorbytes = xor(kbytes, data.getBytes(StandardCharsets.UTF_8));

//        SM4 sm4 = SmUtil.sm4(String.class.getName().getBytes(StandardCharsets.UTF_8));
        String key = String.class.getName();

        byte[] keybs = new byte[16];
        byte[] ivbs = new byte[16];
        buildSm4KeyAndIvBytes(key.getBytes(StandardCharsets.UTF_8), keybs, ivbs);

        byte[] k1 = SecureUtil.md5().digest(key);
        byte[] k2 = SecureUtil.md5().digest(k1);
        SM4 sm4 = new SM4(Mode.CBC, Padding.ZeroPadding, keybs, ivbs);
        String s = sm4.encryptBase64(xorbytes);
        return s;
//
//        String key = String.class.getName();
//        // md5
//        byte[] kbytes = SecureUtil.md5().digest(key);
//        // xor
//        byte[] xorbytes = xor(kbytes, data.getBytes("UTF-8"));
//        // sm4
//        SM4 sm4 = new SM4(Mode.CBC.name(), Padding.ZeroPadding.name(), kbytes, kbytes);
//        String s = sm4.encryptBase64(xorbytes);
//        return s;
    }

    private void buildSm4KeyAndIvBytes(byte[] key, byte[] sm4key, byte[] sm4iv) throws NoSuchAlgorithmException {
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        mdInst.update(key);
        byte[] kb1 = mdInst.digest();
        System.arraycopy(kb1, 0, sm4key, 0, 16);
        if (sm4iv != null) {
            mdInst.reset();
            mdInst.update(kb1);
            byte[] kb2 = mdInst.digest();
            System.arraycopy(kb2, 0, sm4iv, 0, 16);
        }
    }

    private byte[] xor(byte[] key, byte[] data) {
        int i = 0;

        for(int j = 0; i < data.length; ++i) {
            data[i] ^= key[j++];
            if (j >= key.length) {
                j = 0;
            }
        }

        return data;
    }
}
