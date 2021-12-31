package com.xkyss.weixin.tp.service.impl;

import com.xkyss.json.JsonObject;
import com.xkyss.weixin.tp.bean.TpMedia;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.tp.service.TpApiService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.TpMediaService;

import java.io.File;

import static com.xkyss.weixin.tp.constant.TpPathConsts.Media.MEDIA_GET;
import static com.xkyss.weixin.tp.constant.TpPathConsts.Media.MEDIA_UPLOAD;
/**
 * 素材管理服务实现
 *
 * @author xkyii
 * Created on 2021/07/28.
 * Updated on 2021/08/12.
 */
public class TpMediaServiceImpl implements TpMediaService {

    // media_id在同一单位内应用之间可以共享,使用哪个应用的access_token应该都可以
    private final TpContactService contactService;
    private final WxHttpClientService httpClient;

    public TpMediaServiceImpl(TpContactService contactService, WxHttpClientService httpClient) {
        this.contactService = contactService;
        this.httpClient = httpClient;
    }
    @Override
    public TpMedia upload(String fileType, File file) throws TpErrorException {
        String url = contactService.getApiUrl(MEDIA_UPLOAD);
        JsonObject result = httpClient.upload(String.format(url, fileType), file);
        return result.mapTo(TpMedia.class);
    }

    @Override
    public void get(String mediaId) throws TpErrorException {
        String url = contactService.getApiUrl(MEDIA_GET);
        httpClient.get(String.format(url, mediaId));
    }
}
