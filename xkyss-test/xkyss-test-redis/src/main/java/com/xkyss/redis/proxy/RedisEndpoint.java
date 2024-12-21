package com.xkyss.redis.proxy;

import io.vertx.core.Future;
import io.vertx.redis.client.Response;

/**
 * Redis Endpoint 接口
 */
public interface RedisEndpoint {
    Future<Void> send(Response response);
}
