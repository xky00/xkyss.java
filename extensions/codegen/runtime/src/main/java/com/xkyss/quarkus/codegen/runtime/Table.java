package com.xkyss.quarkus.codegen.runtime;

public class Table {
    /**
     * 表名
     */
    private String name;

    /**
     * 类别
     */
    private String category;

    /**
     * 备注(注释)
     */
    private String remarks;

    @Override
    public String toString() {
        return "Table{" +
            "name='" + name + '\'' +
            ", category='" + category + '\'' +
            ", remarks='" + remarks + '\'' +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
