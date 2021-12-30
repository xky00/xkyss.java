package com.xkyss.weixin.common.service.impl;

import com.xkyss.json.Json;
import com.xkyss.json.JsonObject;
import com.xkyss.core.util.StringUtil;
import com.xkyss.weixin.common.property.WxErrorCodeProperties;
import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.common.service.WxHttpClientService;
import okhttp3.*;

import java.io.File;

import static com.xkyss.weixin.common.constant.WxConsts.ACCESS_TOKEN_QUERY_KEY;

/**
 * 政务微信HttpClient服务基于okhttp的实现
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public class WxHttpClientServiceImpl implements WxHttpClientService {

    private final WxErrorCodeProperties errorCodes;
    private final WxCacheService cache;

    public WxHttpClientServiceImpl(WxErrorCodeProperties errorCodes, WxCacheService cache) {
        this.errorCodes = errorCodes;
        this.cache = cache;
    }

    @Override
    public JsonObject get(String url) throws TpErrorException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return execute(request);
    }

    @Override
    public JsonObject post(String url, String content) throws TpErrorException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.Companion.create(content, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return execute(request);
    }

    @Override
    public JsonObject post(String url, Object content) throws TpErrorException {
        if (StringUtil.isNullOrEmpty(content)) {
            return post(url, "");
        }

        return post(url, Json.encode(content));
    }

    @Override
    public JsonObject upload(String url, File file) throws TpErrorException {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.Companion.create(file, mediaType))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return execute(request);
    }

    private JsonObject execute(Request request) throws TpErrorException {
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new TpErrorException(response.code(), response.message());
            }
            JsonObject result = new JsonObject(response.body().string());
            Long errcode = result.getLong("errcode");
            if (errcode != 0) {
                // 当收到token无效、过期等错误时,清理相应的cache值
                if (errorCodes.getRefreshTokenCodes().contains(errcode)) {
                    String access_key = request.url().queryParameter(ACCESS_TOKEN_QUERY_KEY);
                    if (!StringUtil.isNullOrEmpty(access_key)) {
                        cache.del(access_key);
                    }
                    else {
                        throw new TpErrorException(errcode, result.getString("errmsg")
                                + "[" + ACCESS_TOKEN_QUERY_KEY + "未设置]");
                    }
                }

                throw new TpErrorException(errcode, result.getString("errmsg"));
            }
            return result;
        }
        catch (Exception e) {
            if (e instanceof TpErrorException) {
                throw (TpErrorException) e;
            }
            throw new TpErrorException(e.getMessage());
        }
    }
}
