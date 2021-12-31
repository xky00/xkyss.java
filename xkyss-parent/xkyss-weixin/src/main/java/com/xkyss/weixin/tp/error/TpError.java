package com.xkyss.weixin.tp.error;


/**
 * 错误
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public class TpError {

    private Long errcode;

    private String errmsg;

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return String.format("errcode: %d, errmsg: %s", errcode, errmsg);
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long errcode;
        private String errmsg;

        private Builder() {
        }

        public static Builder aZwError() {
            return new Builder();
        }

        public Builder errcode(Long errcode) {
            this.errcode = errcode;
            return this;
        }

        public Builder errmsg(String errmsg) {
            this.errmsg = errmsg;
            return this;
        }

        public TpError build() {
            TpError zwError = new TpError();
            zwError.setErrcode(errcode);
            zwError.setErrmsg(errmsg);
            return zwError;
        }
    }
}
