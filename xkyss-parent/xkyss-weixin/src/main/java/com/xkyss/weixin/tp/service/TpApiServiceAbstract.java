package com.xkyss.weixin.tp.service;

import com.xkyss.core.util.StringUtil;
import com.xkyss.json.JsonObject;
import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.tp.property.TpProperties;

import static com.xkyss.weixin.common.constant.WxConsts.ACCESS_TOKEN_QUERY_KEY;
import static com.xkyss.weixin.tp.constant.TpPathConsts.GET_TOKEN;

/**
 * Api抽象服务,提供基础工具实现
 *
 * @author xkyii
 * Created on 2021/08/12.
 */
public abstract class TpApiServiceAbstract implements TpApiService {

    protected final TpProperties config;
    protected final WxCacheService cache;
    protected final WxHttpClientService httpClient;

    protected TpApiServiceAbstract(TpProperties config, WxCacheService cache, WxHttpClientService httpClient) {
        this.config = config;
        this.cache = cache;
        this.httpClient = httpClient;
    }

    @Override
    public String getApiUrl(String path) throws TpErrorException {
        String url = getBaseApiUrl() + path;
        url += (url.contains("?") ? "&" : "?") + "access_token=" + getAccessToken();
        url += "&" + ACCESS_TOKEN_QUERY_KEY + "=" + getKey();
        return url;
    }

    @Override
    public String getAccessToken() throws TpErrorException {
        String cachedToken = (String) cache.get(getKey());
        if (!StringUtil.isNullOrEmpty(cachedToken)) {
            return cachedToken;
        }

        String url = String.format(getBaseApiUrl() + GET_TOKEN, config.getCorpId(), getSecret());
        JsonObject result = httpClient.get(url);

        String token = result.getString("access_token");
        cache.set(getKey(), token);
        return token;
    }

    protected String getBaseApiUrl() {
        String baseApiUrl = this.config.getServerUrl();
        if (baseApiUrl == null) {
            baseApiUrl = "https://wwlocal.qq.com";
        }
        return baseApiUrl;
    }

    protected abstract String getKey();

    protected abstract String getSecret();
}
