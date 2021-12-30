package com.xkyss.weixin.tp;

import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.impl.WxCacheServiceImpl;
import com.xkyss.weixin.tp.service.TpBatchService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.impl.TpBatchServiceImpl;
import com.xkyss.weixin.common.service.impl.WxHttpClientServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpContactServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BatchTest implements ITest {
    private static TpBatchService batchService;

    @BeforeClass
    public static void beforeClass() {
        WxCacheService cache = new WxCacheServiceImpl();
        WxHttpClientService httpClient = new WxHttpClientServiceImpl(ERROR_CODES, cache);
        TpContactService contact = new TpContactServiceImpl(CONFIG, cache, httpClient);
        batchService = new TpBatchServiceImpl(contact, httpClient);
    }

    @Test
    public void test_01_repalcePartyTest() {

        try {
            batchService.replaceParty("1zR-M5EGf00hOpJXGAwr0V32oZUUlX-TWPZIyGEj_lRJKQ7he9avLoHRtQa5h75sE");
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_01_repalceUserTest() {

        try {
            batchService.replaceUser("1MuShOhf80-fL5COyQLziD_KH7Y27A6Zh8wZIUUjsntnxhEq91-KSrQ65WoV6wpPo");
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
