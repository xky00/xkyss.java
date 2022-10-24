package com.xkyss.json;

import com.xkyss.json.bean.AnoBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonAnnotationTest {
    @Test
    public void test_01() {
        AnoBean ab = new AnoBean();
        ab.setA("aaaa");
        ab.setB("bbbb");

        String s = Json.encodePrettily(ab);

        Assertions.assertNotNull(s);
    }
}