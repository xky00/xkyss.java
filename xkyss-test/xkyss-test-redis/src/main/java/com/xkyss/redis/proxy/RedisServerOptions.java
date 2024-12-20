package com.xkyss.redis.proxy;

import io.vertx.core.net.NetServerOptions;

public class RedisServerOptions extends NetServerOptions {

    @Override
    public RedisServerOptions setPort(int port) {
        super.setPort(port);
        return this;
    }

    @Override
    public RedisServerOptions setHost(String host) {
        super.setHost(host);
        return this;
    }
}
