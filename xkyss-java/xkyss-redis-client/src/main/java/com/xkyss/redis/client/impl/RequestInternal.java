package com.xkyss.redis.client.impl;

import com.xkyss.redis.client.Request;
import io.vertx.core.buffer.Buffer;

interface RequestInternal extends Request {

    Buffer encode(Buffer buffer);

    Buffer encode();

    boolean valid();

    default Request arg(byte[] arg) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default Request arg(Buffer arg) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default Request arg(long arg) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default Request arg(boolean arg) {
        throw new UnsupportedOperationException("Not supported.");
    }

    default Request nullArg() {
        throw new UnsupportedOperationException("Not supported.");
    }
}
