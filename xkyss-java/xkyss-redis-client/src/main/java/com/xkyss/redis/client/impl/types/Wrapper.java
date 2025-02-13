package com.xkyss.redis.client.impl.types;

import com.xkyss.redis.client.Response;
import com.xkyss.redis.client.ResponseType;
import io.vertx.core.buffer.Buffer;


public class Wrapper implements Response {
    private final ResponseType type;
    private final Buffer buffer;

    public Wrapper(ResponseType type, Buffer buffer) {
        this.type = type;
        this.buffer = buffer;
    }

    @Override
    public ResponseType type() {
        return type;
    }

    @Override
    public Buffer toBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public int size() {
        return buffer.length();
    }
}
