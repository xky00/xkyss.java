package com.xkyss.weixin.tp.service.impl;

import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.property.TpProperties;
import com.xkyss.weixin.tp.service.TpApiServiceAbstract;
import com.xkyss.weixin.tp.service.TpAppService;

import static com.xkyss.weixin.tp.constant.TpConsts.WEWORK_TOKEN_PREFIX;

/**
 * 新警务平台服务
 *
 * @author xkyii
 * Created on 2021/08/12.
 */
public class TpMainAppServiceImpl extends TpApiServiceAbstract implements TpAppService {

    public TpMainAppServiceImpl(TpProperties config, WxCacheService cache, WxHttpClientService httpClient) {
        super(config, cache, httpClient);
    }

    @Override
    protected String getKey() {
        return WEWORK_TOKEN_PREFIX + config.getMainApp().getName();
    }

    @Override
    protected String getSecret() {
        return config.getMainApp().getSecret();
    }
}
