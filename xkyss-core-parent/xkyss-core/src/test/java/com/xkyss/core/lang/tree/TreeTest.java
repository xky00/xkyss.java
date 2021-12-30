package com.xkyii.xktool.core.lang.tree;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {

    private static final List<Dept> depts = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        depts.add(new Dept(1L, 0L, "1号部门"));
        depts.add(new Dept(3L, 2L, "3号部门"));
        depts.add(new Dept(2L, 1L, "2号部门"));
    }

    @Test
    public void test01() {
        TreeBuilder<Long, Dept> builder = new TreeBuilder<>();
        builder.setIdGetter(Dept::getId);
        builder.setPidGetter(Dept::getPid);
        builder.add(depts);

        Tree<Long, Dept> tree = builder.build();

        Assert.assertEquals(1L, (long) tree.getId());
    }


    private static class Dept {
        private Long id;
        private Long pid;
        private String name;

        public Dept(Long id, Long pid, String name) {
            this.id = id;
            this.pid = pid;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getPid() {
            return pid;
        }

        public void setPid(Long pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
