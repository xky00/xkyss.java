package com.xkyss.weixin.tp.bean;


public class TpDepartment {
    private String name;
    private Integer parentid;
    private Integer order;
    private Integer id;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentid() {
        return this.parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /// 扩展对象
    private TpDepartment parent;

    public TpDepartment getParent() {
        return parent;
    }

    public void setParent(TpDepartment parent) {
        this.parent = parent;
    }
}

