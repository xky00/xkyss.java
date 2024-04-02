package com.xkyss.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.vertx.core.json.jackson.DatabindCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ObjectMapperTest {
    @Test
    public void test_01() throws JsonProcessingException {
        ObjectMapper om = DatabindCodec.mapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_DOT_CASE);

        String s = om.writeValueAsString(new Foo("nameValue", "codeValue"));
        Assertions.assertEquals("{\"name.key\":\"nameValue\",\"code.key\":\"codeValue\"}", s);
    }

    @Test
    public void test_02() {
        ObjectMapper om = DatabindCodec.mapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_DOT_CASE);

        Map<String, String> map = om
            .convertValue(new Foo("nameValue", "codeValue"), new TypeReference<Map<String, String>>() {});

        Assertions.assertNotNull(map);
        Assertions.assertEquals(2, map.size());
        Assertions.assertTrue(map.containsKey("name.key"));
        Assertions.assertTrue(map.containsKey("code.key"));
    }


    static class Foo {
        public Foo() {

        }

        public Foo(String name, String code) {
            this.nameKey = name;
            this.codeKey = code;
        }
        public String nameKey;
        public String codeKey;
    }
}
