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
    public String toString() {
        return buffer.toString();
    }

    @Override
    public Command command() {
        if (command == null) {
            ReadableBuffer rb = new ReadableBuffer();
            rb.append(buffer);
            rb.skipEOL(); // *1\r\n
            int x = rb.findLineEnd() + 1; // $4\r\n
            rb.skip(x);
            int y = rb.findLineEnd(); // PING\r\n
            rb.setOffset(x);
            String cmd = rb.readLine(y);
            command = Command.create(cmd);
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

    @Override
    public boolean valid() {
        byte first = buffer.getByte(0);
        if (first != '*') {
            return false;
        }
        return this.buffer.length() > 0;
    }
}
