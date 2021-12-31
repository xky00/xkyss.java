package com.xkyss.weixin.tp.service;

import com.xkyss.weixin.tp.bean.TpUser;
import com.xkyss.weixin.tp.bean.TpUserDetail;
import com.xkyss.weixin.tp.error.TpErrorException;

import java.util.List;

/**
 * 政务微信用户服务接口
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpUserService {

    /**
     * 创建成员
     * @return
     */
    void create(TpUser user) throws TpErrorException;

    /**
     * 更新成员
     * @param user
     */
    void update(TpUser user) throws TpErrorException;

    /**
     * 删除成员
     * @param userId
     */
    void delete(String userId) throws TpErrorException;

    /**
     * 读取成员
     * @param userId
     * @return
     */
    TpUser get(String userId) throws TpErrorException;

    /**
     * 获取部门成员
     * @param departmentId
     * @param fetchChild
     * @return
     */
    List<TpUser> simpleList(Integer departmentId, Boolean fetchChild) throws TpErrorException;

    /**
     * 获取部门成员详情
     * @param departmentId
     * @param fetchChild
     * @return
     */
    List<TpUser> list(Integer departmentId, Boolean fetchChild) throws TpErrorException;

    /**
     * 用户用户信息
     * @param code 微信服务器回调的校验码
     * @return 用户信息
     * @throws TpErrorException
     */
    TpUserDetail getUserInfo(String code) throws TpErrorException;
}
