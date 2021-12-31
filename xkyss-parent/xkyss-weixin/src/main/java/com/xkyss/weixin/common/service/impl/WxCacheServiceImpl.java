package com.xkyss.weixin.common.service.impl;

import com.xkyss.core.lang.SimpleCache;
import com.xkyss.weixin.common.service.WxCacheService;

/**
 * 简单缓存,无超时实现
 */
public class WxCacheServiceImpl extends SimpleCache<String, Object> implements WxCacheService {

    @Override
    public void set(String key, Object value) {
        put(key, value);
    }

    @Override
    public Boolean del(String key) {
        try {
            remove(key);
            return Boolean.TRUE;
        }
        catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
