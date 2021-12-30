package com.xkyss.weixin.tp.service.impl;

import com.xkyss.json.JsonArray;
import com.xkyss.json.JsonObject;
import com.xkyss.core.util.BooleanUtil;
import com.xkyss.weixin.tp.bean.TpUser;
import com.xkyss.weixin.tp.bean.TpUserDetail;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.TpAppService;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.TpUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xkyss.weixin.tp.constant.TpPathConsts.User.*;

/**
 * 政务微信用户服务实现
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public class TpUserServiceImpl implements TpUserService {

    private final TpAppService mainAppService;
    private final TpContactService contactService;
    private final WxHttpClientService httpClient;

    public TpUserServiceImpl(TpAppService mainAppService, TpContactService contactService, WxHttpClientService httpClient) {
        this.mainAppService = mainAppService;
        this.contactService = contactService;
        this.httpClient = httpClient;
    }


    @Override
    public void create(TpUser user) throws TpErrorException {
        String url = contactService.getApiUrl(USER_CREATE);
        httpClient.post(url, user);
    }

    @Override
    public void update(TpUser user) throws TpErrorException {
        String url = contactService.getApiUrl(USER_UPDATE);
        httpClient.post(url, user);
    }

    @Override
    public void delete(String userId) throws TpErrorException {
        String url = contactService.getApiUrl(USER_DELETE);
        httpClient.get(String.format(url, userId));
    }

    @Override
    public TpUser get(String userId) throws TpErrorException {
        String url = contactService.getApiUrl(USER_GET);
        JsonObject result = httpClient.get(String.format(url, userId));
        return result.mapTo(TpUser.class);
    }

    @Override
    public List<TpUser> simpleList(Integer departmentId, Boolean fetchChild) throws TpErrorException {
        String url = contactService.getApiUrl(USER_SIMPLE_LIST);
        JsonObject result = httpClient.get(String.format(url, departmentId, BooleanUtil.toInt(fetchChild)));

        JsonArray userlist = result.getJsonArray("userlist");
        List<TpUser> users = new ArrayList<>();
        for (Object val: userlist.getList()) {
            if (val instanceof Map) {
                JsonObject jo = new JsonObject((Map) val);
                users.add(jo.mapTo(TpUser.class));
            }
        }
        return users;
    }

    @Override
    public List<TpUser> list(Integer departmentId, Boolean fetchChild) throws TpErrorException {
        String url = contactService.getApiUrl(USER_LIST);
        JsonObject result = httpClient.get(String.format(url, departmentId, BooleanUtil.toInt(fetchChild)));

        JsonArray userlist = result.getJsonArray("userlist");
        List<TpUser> users = new ArrayList<>();
        for (Object val: userlist.getList()) {
            if (val instanceof Map) {
                JsonObject jo = new JsonObject((Map) val);
                users.add(jo.mapTo(TpUser.class));
            }
        }

        return users;
    }

    @Override
    public TpUserDetail getUserInfo(String code) throws TpErrorException {
        String url = mainAppService.getApiUrl(GET_USER_INFO);
        JsonObject result = httpClient.get(String.format(url, code));
        return getUserDetail(result.getString("user_ticket"));
    }

    private TpUserDetail getUserDetail(String userTicket) throws TpErrorException {
        String url = mainAppService.getApiUrl(GET_USER_DETAIL);
        JsonObject param = new JsonObject().put("user_ticket", userTicket);
        JsonObject result = httpClient.post(url, param);

        return result.mapTo(TpUserDetail.class);
    }
}
