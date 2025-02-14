package com.xkyss.redis.client.impl;

import com.xkyss.redis.client.Command;
import io.vertx.core.buffer.Buffer;

public final class BufferRequest implements RequestInternal {

    private final Buffer buffer;
    private Command command = null;

    public BufferRequest(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public Command command() {
        if (command == null) {

        }
        return command;
    }

    @Override
    public Buffer encode(Buffer buffer) {
        buffer.setBuffer(0, this.buffer);
        return buffer;
    }

    @Override
    public Buffer encode() {
        return this.buffer;
    }
}
