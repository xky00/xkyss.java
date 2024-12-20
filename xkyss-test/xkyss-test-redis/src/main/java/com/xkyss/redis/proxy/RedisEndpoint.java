package com.xkyss.redis.proxy;

/**
 * Redis Endpoint 接口
 */
public interface RedisEndpoint {
    void handle(RedisContext rc);
}
