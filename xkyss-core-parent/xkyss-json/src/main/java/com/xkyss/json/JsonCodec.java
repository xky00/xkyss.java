package com.xkyss.json;

import java.io.Closeable;
import java.io.IOException;

public interface JsonCodec {

    <T> T fromString(String json, Class<T> clazz) throws DecodeException;

    <T> T fromValue(Object json, Class<T> clazz) throws DecodeException;

    default String toString(Object object) throws EncodeException {
        return toString(object, false);
    }

    String toString(Object object, boolean pretty) throws EncodeException;

    static void close(Closeable parser) {
        try {
            parser.close();
        } catch (IOException ignore) {
        }
    }
}
