package com.xkyss.redis.proxy;

import io.vertx.redis.client.Request;

public class RedisContext {

    private Request request;
    private RedisEndpoint endpoint;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RedisContext request(Request request) {
        this.request = request;
        return this;
    }

    public RedisEndpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(RedisEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public RedisContext endpoint(RedisEndpoint endpoint) {
        this.endpoint = endpoint;
        return this;
    }
}
