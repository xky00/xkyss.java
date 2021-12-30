package com.xkyss.weixin.tp.property;

/**
 * 政务微信应用与小程序配置
 * 政务微信/应用与小程序
 *
 * @author xkyii
 * @createdAt  on 2021/07/28.
 */
public class TpAppProperties {
    private String name;
    private String agentId;
    private String secret;

    public TpAppProperties() {
    }

    public TpAppProperties(String agentId, String secret) {
        this.agentId = agentId;
        this.secret = secret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
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
        private String agentId;
        private String secret;

        private Builder() {
        }

        public static Builder aTpAppProperties() {
            return new Builder();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder agentId(String agentId) {
            this.agentId = agentId;
            return this;
        }

        public Builder secret(String secret) {
            this.secret = secret;
            return this;
        }

        public TpAppProperties build() {
            TpAppProperties tpAppProperties = new TpAppProperties();
            tpAppProperties.setName(name);
            tpAppProperties.setAgentId(agentId);
            tpAppProperties.setSecret(secret);
            return tpAppProperties;
        }
    }
}
