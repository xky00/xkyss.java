package com.xkyss.weixin.common.util.crypto;


import com.xkyss.core.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;


/**
 * 政务微信HttpClient服务接口
 * 对请求的结果进行校验,如果errcode不是0,则抛出异常
 *
 * @author xkyii
 * @createdAt 2021/07/28.
 */
public class SHA1 {

    /**
     * 串接arr参数，生成sha1 digest.
     */
    public static String gen(String... arr) {
        if (StringUtil.hasEmpty(arr)) {
            throw new IllegalArgumentException("非法请求参数，有部分参数为空 : " + Arrays.toString(arr));
        }

        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (String a : arr) {
            sb.append(a);
        }
        return DigestUtils.sha1Hex(sb.toString());
    }

    /**
     * 用&串接arr参数，生成sha1 digest.
     */
    public static String genWithAmple(String... arr) {
        if (StringUtil.hasEmpty(arr)) {
            throw new IllegalArgumentException("非法请求参数，有部分参数为空 : " + Arrays.toString(arr));
        }

        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String a = arr[i];
            sb.append(a);
            if (i != arr.length - 1) {
                sb.append('&');
            }
        }
        return DigestUtils.sha1Hex(sb.toString());
    }
}
