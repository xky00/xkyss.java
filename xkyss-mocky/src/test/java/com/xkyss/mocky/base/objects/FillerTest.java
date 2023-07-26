package com.xkyss.mocky.base.objects;

import com.xkyss.mocky.base.objects.bean.SimpleBean;
import com.xkyss.mocky.base.text.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class FillerTest {
    @Test
    public void test_01() {
        Strings s = new Strings(ThreadLocalRandom.current());
        SimpleBean m = new Filler<>(SimpleBean::new)
            .setter(SimpleBean::setS, s)
            .get()
            ;

        Assertions.assertNotNull(m.getS());
    }
}
