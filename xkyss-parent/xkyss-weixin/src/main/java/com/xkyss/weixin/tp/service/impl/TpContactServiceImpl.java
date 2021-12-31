package com.xkyss.weixin.tp.service.impl;

import com.xkyss.core.util.StringUtil;
import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.common.util.crypto.SHA1;
import com.xkyss.weixin.tp.property.TpProperties;
import com.xkyss.weixin.tp.service.TpApiServiceAbstract;
import com.xkyss.weixin.tp.service.TpContactService;

import static com.xkyss.weixin.tp.constant.TpConsts.WEWORK_TOKEN_PREFIX;

/**
 * 通讯录同步及内部登录集成 服务实现
 *
 * @author xkyii
 * Created on 2021/08/12.
 */
public class TpContactServiceImpl extends TpApiServiceAbstract implements TpContactService {

    public TpContactServiceImpl(TpProperties config, WxCacheService cache, WxHttpClientService httpClient) {
        super(config, cache, httpClient);
    }

    @Override
    public boolean checkSignature(String msgSignature, String timestamp, String nonce, String data) {
        try {
            String signature;
            if (StringUtil.isNullOrEmpty(data)) {
                signature = SHA1.gen(this.config.getContact().getToken(), timestamp, nonce);
            }
            else {
                signature = SHA1.gen(this.config.getContact().getToken(), timestamp, nonce, data);
            }
            return signature.equals(msgSignature);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getKey() {
        return WEWORK_TOKEN_PREFIX + "通讯录同步及内部登录集成";
    }

    @Override
    protected String getSecret() {
        return config.getContact().getSecret();
    }
}
