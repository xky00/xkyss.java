package com.xkyss.weixin.tp.bean.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 全量覆盖部门CSV模板表头
 *
 * @author xkyii
 * @createdAt 2021/07/29.
 */
@JsonPropertyOrder({"name", "id", "pid", "sort"})
public class CsvDepartment {
    @JsonProperty("部门名称")
    private String name;
    @JsonProperty("部门ID")
    private Integer id;
    @JsonProperty("父部门ID")
    private Integer pid;
    @JsonProperty("排序")
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
