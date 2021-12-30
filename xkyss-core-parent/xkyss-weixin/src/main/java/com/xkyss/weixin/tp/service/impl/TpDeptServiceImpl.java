package com.xkyss.weixin.tp.service.impl;

import com.xkyss.json.JsonObject;
import com.xkyss.weixin.tp.bean.TpDepartment;
import com.xkyss.weixin.tp.dto.TpDepartmentResult;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.tp.service.TpApiService;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.TpDeptService;
import com.xkyss.weixin.common.service.WxHttpClientService;

import java.util.List;

import static com.xkyss.weixin.tp.constant.TpPathConsts.Department.*;

/**
 * 政务微信部门服务实现
 *
 * @author xkyii
 * Created on 2021/07/28.
 * Updated on 2021/08/12.
 */
public class TpDeptServiceImpl implements TpDeptService {

    private final TpContactService contactService;
    private final WxHttpClientService httpClient;

    public TpDeptServiceImpl(TpContactService contactService, WxHttpClientService httpClient) {
        this.contactService = contactService;
        this.httpClient = httpClient;
    }

    @Override
    public Integer create(TpDepartment dept) throws TpErrorException {
        String url = contactService.getApiUrl(DEPARTMENT_CREATE);
        JsonObject result =  httpClient.post(url, dept);
        return result.getInteger("id");
    }

    @Override
    public void update(TpDepartment dept) throws TpErrorException {
        String url = contactService.getApiUrl(DEPARTMENT_UPDATE);
        httpClient.post(url, dept);
    }

    @Override
    public void delete(Integer deptId) throws TpErrorException {
        String url = contactService.getApiUrl(DEPARTMENT_DELETE);
        httpClient.get(String.format(url, deptId));
    }

    @Override
    public List<TpDepartment> list(Integer deptId) throws TpErrorException {
        String url = contactService.getApiUrl(DEPARTMENT_LIST);
        JsonObject result = httpClient.get(String.format(url, deptId));

        TpDepartmentResult deptResult = result.mapTo(TpDepartmentResult.class);
        return deptResult.getDepartment();
    }
}
