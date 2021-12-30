package com.xkyss.weixin.common.service;

/**
 * 缓存服务
 *
 * @author xkyii
 * @createdAt 2021/08/12.
 */
public interface WxCacheService {
    /**
     * 保存属性
     */
    void set(String key, Object value);

    /**
     * 获取属性
     */
    Object get(String key);

    /**
     * 删除属性
     */
    Boolean del(String key);
}
