package com.xkyss.weixin.tp.error;


/**
 * 错误异常
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public class TpErrorException extends Exception {
    private final TpError error;

    private static final long DEFAULT_ERROR_CODE = -99L;

    public TpErrorException(Number code, String message) {
        this(TpError.builder().errcode(code.longValue()).errmsg(message).build());
    }

    public TpErrorException(String message) {
        this(TpError.builder().errcode(DEFAULT_ERROR_CODE).errmsg(message).build());
    }

    public TpErrorException(TpError error) {
        super(error.toString());
        this.error = error;
    }

    public TpErrorException(TpError error, Throwable cause) {
        super(error.toString(), cause);
        this.error = error;
    }

    public TpErrorException(Throwable cause) {
        super(cause.getMessage(), cause);
        this.error = TpError.builder().errcode(DEFAULT_ERROR_CODE).errmsg(cause.getMessage()).build();
    }

    public TpError getError() {
        return this.error;
    }
}
