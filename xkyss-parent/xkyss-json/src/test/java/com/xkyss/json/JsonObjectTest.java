package com.xkyss.json;

import com.xkyss.json.jackson.DatabindCodec;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonObjectTest {

    @Test
    public void toStringTest() {
        String str = "{\"code\": 500, \"data\":null}";
        JsonObject json = new JsonObject(str);

        Assert.assertEquals(json.getInteger("code").intValue(), 500);
        Assert.assertNull(json.getValue("data"));
    }

    @Test
    public void toStringTest2() {
        String str = "{\"test\":\"添加一个 README.md 文件，帮助感兴趣的人了解\"}";
        //noinspection MismatchedQueryAndUpdateOfCollection
        JsonObject json = new JsonObject(str);
        Assert.assertEquals(str, json.toString());
    }

    @Test
    public void toStringTest3() {
        JsonObject json = new JsonObject()
                .put("test", "9999ffff")
                .put("data", "22222ttt");
        String str = json.toString();
        Assert.assertEquals(str, "{\"test\":\"9999ffff\",\"data\":\"22222ttt\"}");
    }

    @Test
    public void toStringTest4() {
        JsonArray ja = new JsonArray();
        ja.add(1);
        ja.add("22");

        String str = ja.toString();
        Assert.assertEquals(str, "[1,\"22\"]");
    }

    @Test
    public void dateTest5() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2000-11-11 11:11:11");

        JsonObject jo = new JsonObject();
        jo.put("d", date);
        Assert.assertEquals("{\"d\":973912271000}", jo.toString());

        DatabindCodec codec = new DatabindCodec();
        codec.mapper().setDateFormat(new SimpleDateFormat("yyyyMMddHHmmss"));
        jo.setCodec(codec);
        Assert.assertEquals("{\"d\":\"20001111111111\"}", jo.toString());
    }
}
