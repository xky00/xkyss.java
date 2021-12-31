package com.xkyss.weixin.tp.property;

/**
 * 应用管理 配置
 * 政务微信/管理工具/应用管理
 *
 * @author xkyii
 * @createdAt 2021/08/12.
 */
public class TpAppManagerProperties {
    /**
     * 名称
     */
    private String name;

    /**
     * 密钥
     */
    private String secret;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String secret;

        private Builder() {
        }

        public static Builder aTpAppManagerProperties() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public TpAppManagerProperties build() {
            TpAppManagerProperties tpAppManagerProperties = new TpAppManagerProperties();
            tpAppManagerProperties.setName(name);
            tpAppManagerProperties.setSecret(secret);
            return tpAppManagerProperties;
        }
    }
}
