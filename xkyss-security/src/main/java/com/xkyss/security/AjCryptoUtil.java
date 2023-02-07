package com.xkyss.security;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AjCryptoUtil {

    public static String sm4Encrypt(String xorKey, String data) {
        // md5
        byte[] kbytes = SecureUtil.md5().digest(xorKey);
        // xor
        byte[] xorbytes = xor(kbytes, data.getBytes(StandardCharsets.UTF_8));

        String key = String.class.getName();

        byte[] k1 = SecureUtil.md5().digest(key);
        byte[] k2 = SecureUtil.md5().digest(k1);
        String s1 = new SM4(Mode.CBC, Padding.PKCS5Padding, k1, k2).encryptBase64(xorbytes);
        return s1;
    }

    public static String md5AndBase64(String password) {
        byte[] digest = SecureUtil.md5().digest(password, StandardCharsets.UTF_8);
        String encoded = Base64.getEncoder().encodeToString(digest);
        return encoded;
    }

    private static byte[] xor(byte[] key, byte[] data) {
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
