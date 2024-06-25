package com.xkyss.lang.tree;


import com.xkyss.core.lang.tree.Tree;
import com.xkyss.core.lang.tree.TreeBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {

    private static final List<Dept> depts = new ArrayList<>();

    @BeforeAll
    public static void beforeClass() {
        // 0
        // -- 1
        //    -- 21
        //       -- 3211
        //    -- 22
        //       -- 3221
        //       -- 3222
        //       -- 3223
        //    -- 23
        depts.add(new Dept(1L, 0L, "部门1"));
        depts.add(new Dept(23L, 1L, "部门2-2"));
        depts.add(new Dept(3211L, 21L, "部门3-21-1"));
        depts.add(new Dept(3221L, 22L, "部门3-22-1"));
        depts.add(new Dept(22L, 1L, "部门2-2"));
        depts.add(new Dept(3222L, 22L, "部门3-22-2"));
        depts.add(new Dept(3223L, 22L, "部门3-22-3"));
        depts.add(new Dept(21L, 1L, "部门2-1"));
    }

    @Test
    public void test01() {
        TreeBuilder<Long, Dept> builder = new TreeBuilder<>();
        builder.setIdGetter(Dept::getId);
        builder.setPidGetter(Dept::getPid);
        builder.add(depts);

        Tree<Long, Dept> tree = builder.build();

        Assertions.assertEquals(1L, (long) tree.getId());
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
