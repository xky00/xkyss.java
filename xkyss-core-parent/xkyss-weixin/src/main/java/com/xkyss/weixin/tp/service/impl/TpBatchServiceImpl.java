package com.xkyss.weixin.tp.service.impl;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.xkyss.core.lang.Assert;
import com.xkyss.json.JsonObject;
import com.xkyss.weixin.tp.error.TpErrorException;
import com.xkyss.weixin.tp.service.TpApiService;
import com.xkyss.weixin.tp.service.TpBatchService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.TpContactService;

import java.io.File;
import java.util.List;

import static com.xkyss.weixin.tp.constant.TpPathConsts.*;

/**
 * 异步批量接口服务实现
 *
 * @author xkyii
 * Created on 2021/07/28.
 * Updated on 2021/08/12.
 */
public class TpBatchServiceImpl implements TpBatchService {

    private final TpContactService contactService;
    private final WxHttpClientService httpClient;

    public TpBatchServiceImpl(TpContactService contactService, WxHttpClientService httpClient) {
        this.contactService = contactService;
        this.httpClient = httpClient;
    }

    @Override
    public String replaceParty(String mediaId) throws TpErrorException {
        String url = contactService.getApiUrl(BATCH_REPLACE_PARTY);
        JsonObject jo = new JsonObject();
        jo.put("media_id", mediaId);
        JsonObject result = httpClient.post(url, jo.toString());
        return result.getString("jobid");
    }

    @Override
    public String replaceUser(String mediaId) throws TpErrorException {
        String url = contactService.getApiUrl(BATCH_REPLACE_USER);
        JsonObject jo = new JsonObject();
        jo.put("media_id", mediaId);
        JsonObject result = httpClient.post(url, jo.toString());
        return result.getString("jobid");
    }

    @Override
    public <T> void saveToCsv(Class<T> clazz, List<T> departments, File saveTo) throws TpErrorException {
        Assert.notEmpty(departments, "数据列表为空.");
        Assert.notNull(saveTo, "文件为空");

        CsvMapper mapper = new CsvMapper();
        // 严格检查引号,可避免长度超过24时自动加上引号的问题
        mapper.configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true);
        try {
            SequenceWriter writer = mapper
                    .writer(mapper.schemaFor(clazz).withHeader())
                    .writeValues(saveTo);
            for (Object dept: departments) {
                writer.write(dept);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new TpErrorException(e.getMessage());
        }
    }
}
