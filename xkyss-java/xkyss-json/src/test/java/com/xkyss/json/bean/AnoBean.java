package com.xkyss.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AnoBean {

    private String a;

    @JsonIgnore
    private String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }
}