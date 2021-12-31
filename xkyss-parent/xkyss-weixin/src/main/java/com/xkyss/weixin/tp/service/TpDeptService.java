package com.xkyss.weixin.tp.service;

import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.tp.bean.TpDepartment;

import java.util.List;

/**
 * 政务微信部门服务接口
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpDeptService {

    /**
     * 创建部门
     * @param dept
     * @return
     */
    Integer create(TpDepartment dept) throws TpErrorException;

    /**
     * 更新部门
     * @param dept
     */
    void update(TpDepartment dept) throws TpErrorException;

    /**
     * 删除部门
     * @param deptId
     */
    void delete(Integer deptId) throws TpErrorException;

    /**
     * 获取部门列表
     * @param deptId
     * @return
     */
    List<TpDepartment> list(Integer deptId) throws TpErrorException;
}
