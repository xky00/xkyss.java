package com.xkyss.redis.client.impl;

import io.vertx.core.buffer.Buffer;

/**
 * @author xkyss
 */
public class BufferMessage {

    protected Buffer buffer;

    public int length() {
        return buffer.length();
    }

    public byte[] toBytes() {
        return buffer.getBytes();
    }

    public String toString() {
        return buffer.toString();
    }
}
