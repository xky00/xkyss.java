package com.xkyss.mocky.unit.objects;

import com.xkyss.mocky.unit.objects.bean.SimpleBean;
import com.xkyss.mocky.unit.text.Strings;
import com.xkyss.mocky.unit.types.Ints;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class FillerTest {
    @Test
    public void test_01() {
        Strings s = Strings.defaultOf(ThreadLocalRandom.current());
        SimpleBean m = new Filler<>(SimpleBean::new)
            .setter(SimpleBean::setS, s)
            .get()
            ;

        Assertions.assertNotNull(m.getS());
    }
}
