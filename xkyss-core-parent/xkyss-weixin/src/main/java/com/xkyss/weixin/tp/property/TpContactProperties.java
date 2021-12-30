package com.xkyss.weixin.tp.property;

/**
 * 通讯录配置
 * 政务微信/管理工具/通讯录同步及内部登录集成
 *
 * @author xkyii
 * @createdAt 2021/08/05.
 */
public class TpContactProperties {
    private String secret;
    private String token;
    private String encodingAesKey;

    public TpContactProperties() {
    }

    public TpContactProperties(String secret, String token, String encodingAesKey) {
        this.secret = secret;
        this.token = token;
        this.encodingAesKey = encodingAesKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncodingAesKey() {
        return encodingAesKey;
    }

    public void setEncodingAesKey(String encodingAesKey) {
        this.encodingAesKey = encodingAesKey;
    }

    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String secret;
        private String token;
        private String encodingAesKey;

        private Builder() {
        }

        public static Builder aTpContactProperties() {
            return new Builder();
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder encodingAesKey(String encodingAesKey) {
            this.encodingAesKey = encodingAesKey;
            return this;
        }

        public TpContactProperties build() {
            TpContactProperties tpContactProperties = new TpContactProperties();
            tpContactProperties.setSecret(secret);
            tpContactProperties.setToken(token);
            tpContactProperties.setEncodingAesKey(encodingAesKey);
            return tpContactProperties;
        }
    }
}
