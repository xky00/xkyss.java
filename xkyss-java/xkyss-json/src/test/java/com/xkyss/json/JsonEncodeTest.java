package com.xkyss.json;


import com.xkyss.json.bean.Duration;
import com.xkyss.json.bean.Foo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JsonEncodeTest {
    public class TestBean {
        private Long iDurationObjId;
        private Long fooObjId;

        public Long getiDurationObjId() {
            return iDurationObjId;
        }

        public void setiDurationObjId(Long iDurationObjId) {
            this.iDurationObjId = iDurationObjId;
        }

        public Long getFooObjId() {
            return fooObjId;
        }

        public void setFooObjId(Long fooObjId) {
            this.fooObjId = fooObjId;
        }

        private Duration duration;
        private Foo foo;

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Foo getFoo() {
            return foo;
        }

        public void setFoo(Foo foo) {
            this.foo = foo;
        }
    }

    @Test
    public void test_01() {
        Duration duration = new Duration();
        duration.setObjId(7430100000000195L);
        duration.setStartTime("1640654751647");
        duration.setEndTime("1643246751647");

        Foo foo = new Foo();
        foo.setObjId(7430100000000195L);
        foo.setStartTime("1640654751647");
        foo.setEndTime("1643246751647");

        TestBean tb = new TestBean();
        tb.setFooObjId(foo.getObjId());
        tb.setiDurationObjId(duration.getObjId());
        tb.setDuration(duration);
        tb.setFoo(foo);

        String jsonString = Json.encode(tb);

        Assertions.assertNotNull(jsonString);
    }
}