package com.xkyss.weixin.common.constant;

public interface WxConsts {
    /**
     * 附加到url中,用来索引access_token在cache中的key
     * 如请求url为:
     *      http://www.localqq.com/user/list?access_token=ACCESS_TOKEN_VALUE&__access_key=akey
     *      则,取得`akey`作为CacheService.get()的参数,可以获取到缓存在cache中的access_token值
     */
    String ACCESS_TOKEN_QUERY_KEY = "__access_key";
}
