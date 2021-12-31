package com.xkyss.weixin.common.util.crypto;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 * 对公众平台发送给公众账号的消息加解密示例代码.
 * Copyright (c) 1998-2014 Tencent Inc.
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 * </pre>
 *
 * @author Tencent
 */
public interface WxCryptUtil {
    Base64 BASE64 = new Base64();
    Charset CHARSET = StandardCharsets.UTF_8;
}
