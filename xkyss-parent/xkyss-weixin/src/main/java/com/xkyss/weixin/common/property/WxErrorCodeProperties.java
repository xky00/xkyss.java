package com.xkyss.weixin.common.property;

import java.util.List;

/**
 * 错误号
 *
 * @author xkyii
 * @createdAt 2021/08/25.
 */
public class WxErrorCodeProperties {

    /**
     * 收到这些错误号时,需要刷新token
     * 已知:
     *  42001: access_token expired
     */
    private List<Long> refreshTokenCodes;

    public List<Long> getRefreshTokenCodes() {
        return refreshTokenCodes;
    }

    public void setRefreshTokenCodes(List<Long> refreshTokenCodes) {
        this.refreshTokenCodes = refreshTokenCodes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<Long> refreshToken;

        private Builder() {
        }

        public static Builder aWxErrorCodeProperties() {
            return new Builder();
        }

        public Builder refreshToken(List<Long> refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public WxErrorCodeProperties build() {
            WxErrorCodeProperties wxErrorCodeProperties = new WxErrorCodeProperties();
            wxErrorCodeProperties.setRefreshTokenCodes(refreshToken);
            return wxErrorCodeProperties;
        }
    }
}
