package com.xkyss.redis.proxy;

import io.vertx.redis.client.Request;

public class RedisContext {

    private Request request;

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
}
