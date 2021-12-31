package com.xkyss.weixin.tp;

import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.impl.WxCacheServiceImpl;
import com.xkyss.weixin.tp.bean.TpMedia;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.TpMediaService;
import com.xkyss.weixin.common.service.impl.WxHttpClientServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpContactServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpMediaServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MediaTest implements ITest {
    private static TpMediaService mediaService;

    @BeforeClass
    public static void beforeClass() {
        WxCacheService cache = new WxCacheServiceImpl();
        WxHttpClientService httpClient = new WxHttpClientServiceImpl(ERROR_CODES, cache);
        TpContactService contact = new TpContactServiceImpl(CONFIG, cache, httpClient);
        mediaService = new TpMediaServiceImpl(contact, httpClient);
    }

    @Test
    public void test_01_uploadTest() {
        File file = new File("C:/Users/xk/Downloads/部门列表.csv");

        try {
            TpMedia media = mediaService.upload("file", file);
            System.out.print(media);
            Assert.assertNotNull(media);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_02_uploadTest() {
        File file = new File("C:/Users/xk/Downloads/用户列表.csv");

        try {
            TpMedia media = mediaService.upload("file", file);
            System.out.print(media);
            Assert.assertNotNull(media);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_03_getTest() {
        try {
            mediaService.get("1zR-M5EGf00hOpJXGAwr0V32oZUUlX-TWPZIyGEj_lRJKQ7he9avLoHRtQa5h75sE");
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
