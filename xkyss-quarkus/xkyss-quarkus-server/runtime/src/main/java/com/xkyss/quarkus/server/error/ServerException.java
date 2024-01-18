package com.xkyss.quarkus.server.error;

public class ServerException extends RuntimeException {
    private final Integer code;

    public ServerException(Integer code) {
        this.code = code;
    }

    public ServerException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServerException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public ServerException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
