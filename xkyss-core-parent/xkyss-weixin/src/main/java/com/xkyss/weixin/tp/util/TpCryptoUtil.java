package com.xkyss.weixin.tp.util;

import com.xkyss.weixin.common.error.WxRuntimeException;
import com.xkyss.weixin.common.util.crypto.ByteGroup;
import com.xkyss.weixin.common.util.crypto.PKCS7Encoder;
import com.xkyss.weixin.common.util.crypto.WxCryptUtil;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TpCryptoUtil implements WxCryptUtil {

    /**
     * 对明文进行加密.
     * @param encodingAesKey 开发者设置的EncodingAESKey
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     * @throws WxRuntimeException
     */
    public static String encrypt(String encodingAesKey, String text) throws WxRuntimeException {
        byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");

        ByteGroup byteCollector = new ByteGroup();
        byte[] textBytes = text.getBytes(CHARSET);

        byteCollector.addBytes(textBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = BASE64.encodeToString(encrypted);

            return base64Encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
