package com.xkyss.weixin.tp.service;

/**
 * 通讯录 接口
 *
 * @author xkyii
 * Created on 2021/08/12.
 */
public interface TpContactService extends TpApiService {
    /**
     * <pre>
     * 验证推送过来的消息的正确性
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=验证消息真实性
     * </pre>
     *
     * @param msgSignature 消息签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param data         微信传输过来的数据，有可能是echoStr，有可能是xml消息
     * @return the boolean
     */
    boolean checkSignature(String msgSignature, String timestamp, String nonce, String data);
}
