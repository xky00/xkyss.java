package com.xkyss.json;

/**
 * Json解码异常
 *
 * @author xkyii
 * Create on 2021/07/27.
 */
public class DecodeException extends RuntimeException {

    public DecodeException() {
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
