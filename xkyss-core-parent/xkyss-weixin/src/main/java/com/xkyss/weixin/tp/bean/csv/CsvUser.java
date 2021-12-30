package com.xkyss.weixin.tp.bean.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 全量覆盖用户CSV模板表头
 *
 * @author xkyii
 * @createdAt 2021/07/29.
 */

@JsonPropertyOrder({"name", "userid", "mobile", "email", "departmentId", "position", "gender", "isLeader", "sort", "aliasName", "telephone", "disabled", "password"})
public class CsvUser {
    @JsonProperty("姓名")
    private String name;

    @JsonProperty("帐号")
    private String userid;

    @JsonProperty("手机号")
    private String mobile;

    @JsonProperty("邮箱")
    private String email;

    @JsonProperty("所在部门")
    private Integer departmentId;

    @JsonProperty("职位")
    private String position;

    @JsonProperty("性别")
    private Integer gender;

    @JsonProperty("是否领导")
    private Integer leader;

    @JsonProperty("排序")
    private Integer sort;

    @JsonProperty("别名")
    private String aliasName;

    @JsonProperty("座机")
    private String telephone;

    @JsonProperty("禁用")
    private Integer disabled;

    @JsonProperty("初始密码")
    private String password;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }
}
