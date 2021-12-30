package com.xkyss.weixin.tp.service;

import com.xkyss.weixin.tp.error.TpErrorException;

/**
 * 政务微信Api服务接口
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpApiService {

    /**
     * 获取ApiUrl
     * @param path route路径
     * @return
     */
    String getApiUrl(String path) throws TpErrorException;

    /**
     * access_token
     * @return access_token
     */
    String getAccessToken() throws TpErrorException;

}
