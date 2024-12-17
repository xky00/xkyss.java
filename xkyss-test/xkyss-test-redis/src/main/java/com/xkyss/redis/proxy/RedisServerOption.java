package com.xkyss.redis.proxy;

import io.vertx.core.net.NetServerOptions;

public class RedisServerOption extends NetServerOptions {

    @Override
    public RedisServerOption setPort(int port) {
        super.setPort(port);
        return this;
    }

    @Override
    public RedisServerOption setHost(String host) {
        super.setHost(host);
        return this;
    }
}
