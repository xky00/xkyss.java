package com.xkyss.weixin.common.service;

import com.xkyss.json.JsonObject;
import com.xkyss.weixin.tp.error.TpErrorException;

import java.io.File;

/**
 * 政务微信HttpClient服务接口
 * 对请求的结果进行校验,如果errcode不是0,则抛出异常
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface WxHttpClientService {

    JsonObject get(String url) throws TpErrorException;

    JsonObject post(String url, String content) throws TpErrorException;

    JsonObject post(String url, Object content) throws TpErrorException;

    JsonObject upload(String url, File file) throws TpErrorException;
}
