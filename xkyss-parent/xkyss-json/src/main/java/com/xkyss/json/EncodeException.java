package com.xkyss.json;

/**
 * Json编码异常
 *
 * @author xkyii
 * Create on 2021/07/27.
 */
public class EncodeException extends RuntimeException {

    public EncodeException(String message) {
        super(message);
    }

    public EncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncodeException() {
    }
}