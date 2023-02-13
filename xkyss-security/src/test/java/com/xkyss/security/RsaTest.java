package com.xkyss.security;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RsaTest {
    @Test
    public void test_publicKey() throws IOException, ClassNotFoundException {
        String publicKeyString = "rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAAAXjBcMA0GCSqGSIb3DQEBAQUAA0sAMEgCQQCCmRBRz4oUehsuHrZY9MYpYXl8SZ2wh8a7PgGu6abkDevg6xWJ5RuOBprnItYZU+8cFsNFm//1qne4QZmR/A6zAgMBAAF0AAVYLjUwOX5yABlqYXZhLnNlY3VyaXR5LktleVJlcCRUeXBlAAAAAAAAAAASAAB4cgAOamF2YS5sYW5nLkVudW0AAAAAAAAAABIAAHhwdAAGUFVCTElD";

        // base64解码
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        PublicKey publicKey = (PublicKey) (new ObjectInputStream(new ByteArrayInputStream(publicKeyBytes))).readObject();

        // 导出为pem
        RsaUtil.writePem(publicKey, "publicKey.pem");
    }

    @Test
    public void test_privateKey() throws IOException, ClassNotFoundException {
        String publicKeyString = "rO0ABXNyABRqYXZhLnNlY3VyaXR5LktleVJlcL35T7OImqVDAgAETAAJYWxnb3JpdGhtdAASTGphdmEvbGFuZy9TdHJpbmc7WwAHZW5jb2RlZHQAAltCTAAGZm9ybWF0cQB+AAFMAAR0eXBldAAbTGphdmEvc2VjdXJpdHkvS2V5UmVwJFR5cGU7eHB0AANSU0F1cgACW0Ks8xf4BghU4AIAAHhwAAABWDCCAVQCAQAwDQYJKoZIhvcNAQEBBQAEggE+MIIBOgIBAAJBAIKZEFHPihR6Gy4etlj0xilheXxJnbCHxrs+Aa7ppuQN6+DrFYnlG44Gmuci1hlT7xwWw0Wb//Wqd7hBmZH8DrMCAwEAAQJATw/Q1GY7JnhhmgMXyzSr/oM3I6oBX8xi4BoCtNxYn3h8YMOG81DcPpqzJeMpv++5GMZR3Ud0h/0nMKRH4ut3sQIhAMDAOIJBPwjfZJep/tnyOnH5QN6g3entVqc0yciNiO/7AiEArXPERrNxrPg10emmjwC6+tu/F7RD1vG9T33+S2Y1RqkCIFsMMWUtxsW9KDoP3cc7iWn+8Cp5WHnAV5dB8zLd0FpzAiEAlwI/8pHNuRqLuMpkAQJQx5BDST7fBSaHe8qkdz5vyakCIAqRBGLfcni3VGh9+v5FZKEpvv6VuiNi0vlaQjNyipbxdAAGUEtDUyM4fnIAGWphdmEuc2VjdXJpdHkuS2V5UmVwJFR5cGUAAAAAAAAAABIAAHhyAA5qYXZhLmxhbmcuRW51bQAAAAAAAAAAEgAAeHB0AAdQUklWQVRF";

        // base64解码
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        PrivateKey key = (PrivateKey) (new ObjectInputStream(new ByteArrayInputStream(publicKeyBytes))).readObject();

        // 导出为pem
        RsaUtil.writePem(key, "privateKey.pem");
    }
}
