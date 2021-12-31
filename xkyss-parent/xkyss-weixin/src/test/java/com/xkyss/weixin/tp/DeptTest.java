package com.xkyss.weixin.tp;

import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.impl.WxCacheServiceImpl;
import com.xkyss.weixin.tp.bean.TpDepartment;
import com.xkyss.weixin.tp.service.TpContactService;
import com.xkyss.weixin.tp.service.TpDeptService;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.impl.TpContactServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpDeptServiceImpl;
import com.xkyss.weixin.common.service.impl.WxHttpClientServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeptTest implements ITest {

    private static TpDeptService deptService;

    @BeforeClass
    public static void beforeClass() {
        WxCacheService cache = new WxCacheServiceImpl();
        WxHttpClientService httpClient = new WxHttpClientServiceImpl(ERROR_CODES, cache);
        TpContactService contact = new TpContactServiceImpl(CONFIG, cache, httpClient);
        deptService = new TpDeptServiceImpl(contact, httpClient);
    }

    @Test
    public void test_01_CreateDept() {
        try {
            TpDepartment dept = new TpDepartment();
            dept.setId(10100001);
            dept.setOrder(1);
            dept.setParentid(1);
            dept.setName("后端组");

            Integer id = deptService.create(dept);
            Assert.assertEquals(id.longValue(), 10100001);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_02_UpdateDept() {
        try {
            TpDepartment dept = new TpDepartment();
            dept.setId(10100001);
            dept.setOrder(1);
            dept.setParentid(1);
            dept.setName("后端组1");

            deptService.update(dept);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_03_ListDept() {
        try {
            List<TpDepartment> depts = deptService.list(10100001);
            Assert.assertTrue(depts.size() > 0);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_04_DeleteDept() {

        try {
            deptService.delete(10100001);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
