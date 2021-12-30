package com.xkyss.weixin.common.error;

public class WxRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 4881698471192264412L;

    public WxRuntimeException(Throwable e) {
        super(e);
    }

    public WxRuntimeException(String msg) {
        super(msg);
    }

    public WxRuntimeException(String msg, Throwable e) {
        super(msg, e);
    }
}

