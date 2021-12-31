package com.xkyss.weixin.tp.service;

import com.xkyss.weixin.tp.bean.TpMedia;
import com.xkyss.weixin.tp.error.TpErrorException;

import java.io.File;

/**
 * 素材管理服务接口
 *
 * @author xkyii
 * Created on 2021/07/28.
 */
public interface TpMediaService {
    /**
     * 上传临时素材文件
     * 素材上传得到media_id，该media_id仅3天内有效
     * media_id在同一单位内应用之间可以共享
     *
     * @see http://192.168.0.71/api/doc#10112
     *
     * @param fileType 媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)
     * @param file
     * @return
     */
    TpMedia upload(String fileType, File file) throws TpErrorException;

    /**
     * 获取媒体文件内容
     * TODO: FixMe: 响应不是json
     *
     * @param mediaId
     * @throws TpErrorException
     */
    void get(String mediaId) throws TpErrorException;
}
