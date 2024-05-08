package com.xkyss.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SetTest {
    @Test
    public void test_add() {
        Set<BanRule> rules = new HashSet<>();

        BanRule r1 = new BanRule();
        r1.setRule("abc");
        r1.setPrefix(true);
        rules.add(r1);

        BanRule r2 = new BanRule();
        r2.setRule("abc");
        r2.setPrefix(true);
        rules.add(r2);

        Assertions.assertEquals(2, rules.size());
    }

    @Test
    public void test_add2() {
        Set<BanRule2> rules = new HashSet<>();

        BanRule2 r1 = new BanRule2();
        r1.setRule("abc");
        r1.setPrefix(true);
        rules.add(r1);

        BanRule2 r2 = new BanRule2();
        r2.setRule("abc");
        r2.setPrefix(true);
        rules.add(r2);

        Assertions.assertEquals(1, rules.size());
    }

    static class BanRule {
        private String rule;
        private boolean prefix;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public boolean isPrefix() {
            return prefix;
        }

        public void setPrefix(boolean prefix) {
            this.prefix = prefix;
        }
    }

    static class BanRule2 {
        private String rule;
        private boolean prefix;

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public boolean isPrefix() {
            return prefix;
        }

        public void setPrefix(boolean prefix) {
            this.prefix = prefix;
        }

        // equals 方法重写
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BanRule2 that = (BanRule2) o;
            return this.prefix == that.prefix && Objects.equals(this.rule, that.rule);
        }

        // hashCode 方法重写
        @Override
        public int hashCode() {
            return Objects.hash(rule, prefix);
        }
    }
}
