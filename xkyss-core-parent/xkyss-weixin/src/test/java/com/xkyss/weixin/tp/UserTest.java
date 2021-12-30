package com.xkyss.weixin.tp;

import com.xkyss.weixin.common.service.WxCacheService;
import com.xkyss.weixin.common.service.impl.WxCacheServiceImpl;
import com.xkyss.weixin.tp.bean.TpDepartment;
import com.xkyss.weixin.tp.bean.TpUser;
import com.xkyss.weixin.tp.service.*;
import com.xkyss.weixin.common.service.WxHttpClientService;
import com.xkyss.weixin.tp.service.impl.TpContactServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpDeptServiceImpl;
import com.xkyss.weixin.common.service.impl.WxHttpClientServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpMainAppServiceImpl;
import com.xkyss.weixin.tp.service.impl.TpUserServiceImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest implements ITest {

    private static TpUserService userService;
    private static TpDeptService deptService;

    @BeforeClass
    public static void beforeClass() {
        WxCacheService cache = new WxCacheServiceImpl();
        WxHttpClientService httpClient = new WxHttpClientServiceImpl(ERROR_CODES, cache);
        TpContactService contact = new TpContactServiceImpl(CONFIG, cache, httpClient);
        TpAppService mainApp = new TpMainAppServiceImpl(CONFIG, cache, httpClient);
        deptService = new TpDeptServiceImpl(contact, httpClient);
        userService = new TpUserServiceImpl(mainApp, contact, httpClient);
    }

    @Test
    public void test_01_CreateDept() {
        try {
            TpDepartment dept = new TpDepartment();
            dept.setId(10100001);
            dept.setOrder(1);
            dept.setParentid(10000001);
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
    public void test_02_CreateUser() {
        try {
            TpUser user = new TpUser();
            user.setUserid("zhangsan");
            user.setName("张三");
            user.setEnglish_name("jackzhang");
            user.setMobile("15988776611");
            user.setDepartment(new Integer[] { 10100001 });
            user.setOrder(new Integer[] { 100 });
            user.setGender(1);
            user.setEmail("zhangsan@xkyii.com");
            user.setIs_leader_in_dept(new int[] { 0 });
            user.setEnable(1);
            user.setInitpwd("1234abcd");
            user.setIsnotify(1);
            user.setTelephone("020-112233");

            userService.create(user);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_03_UpdateUser() {
        try {
            TpUser user = new TpUser();
            user.setUserid("zhangsan");
            user.setName("张三");
            user.setEnglish_name("jackyzhang");
            user.setMobile("15988776655");
            user.setDepartment(new Integer[] { 10100001 });
            user.setOrder(new Integer[] { 100 });
            user.setGender(1);
            user.setEmail("zhangsan@xkyii.com");
            user.setIs_leader_in_dept(new int[] { 0 });
            user.setEnable(1);
            user.setInitpwd("1234abcd");
            user.setIsnotify(1);
            user.setTelephone("020-112233");

            userService.update(user);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_04_GetUser() {
        try {
            TpUser user = userService.get("zhangsan");
            Assert.assertEquals(user.getUserid(), "zhangsan");
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_05_SimpleListUser() {
        try {
            List<TpUser> users = userService.simpleList(10100001, false);
            Assert.assertTrue(users.size() > 0);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_06_ListUser() {
        try {
            List<TpUser> users = userService.list(10100001, false);
            Assert.assertTrue(users.size() > 0);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_98_DeleteUser() {
        try {
            userService.delete("zhangsan");
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void test_99_DeleteDept() {
        try {
            deptService.delete(10100001);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
