package com.xkyss.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.vertx.core.json.jackson.DatabindCodec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {
    @Test
    public void test_01() throws JsonProcessingException {
        ObjectMapper om = DatabindCodec.mapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_DOT_CASE);

        String s = om.writeValueAsString(new Foo("nameValue", "codeValue"));
        Assertions.assertEquals("{\"name.key\":\"nameValue\",\"code.key\":\"codeValue\"}", s);
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
