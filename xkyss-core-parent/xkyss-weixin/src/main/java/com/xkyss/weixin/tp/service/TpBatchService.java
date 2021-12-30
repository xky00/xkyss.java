package com.xkyss.weixin.tp.service;

import com.xkyss.weixin.tp.bean.csv.CsvDepartment;
import com.xkyss.weixin.tp.error.TpErrorException;

import java.io.File;
import java.util.List;

/**
 * 异步批量接口服务接口
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpBatchService {

    /**
     * 全量覆盖部门
     * @param mediaId 上传的csv文件的media_id
     * @return jobid
     * @throws TpErrorException
     */
    String replaceParty(String mediaId) throws TpErrorException;

    /**
     * replaceuser
     * @param mediaId 上传的csv文件的media_id
     * @return jobid
     * @throws TpErrorException
     */
    String replaceUser(String mediaId) throws TpErrorException;

    /**
     * 保存数据为CSV
     * @param clazz 数据类型
     * @param data 数据列表
     * @param saveTo 保存到这个文件
     * @throws TpErrorException
     */
    public <T> void saveToCsv(Class<T> clazz, List<T> data, File saveTo) throws TpErrorException;

}
