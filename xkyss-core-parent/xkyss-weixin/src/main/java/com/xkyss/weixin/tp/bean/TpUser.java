package com.xkyss.weixin.tp.bean;


public class TpUser {
    private String userid;
    private String name;
    private String english_name;
    private String mobile;
    private Integer[] department;
    private Integer[] order;
    private String[] positions;
    private int gender;
    private String email;
    private int[] is_leader_in_dept;
    private int enable;
    private String initpwd;
    private int isnotify;
    private String avatar_mediaid;
    private String telephone;
    private Extattr extattr;

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglish_name() {
        return this.english_name;
    }

    public void setEnglish_name(String english_name) {
        this.english_name = english_name;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer[] getDepartment() {
        return this.department;
    }

    public void setDepartment(Integer[] department) {
        this.department = department;
    }

    public Integer[] getOrder() {
        return this.order;
    }

    public void setOrder(Integer[] order) {
        this.order = order;
    }

    public String[] getPositions() {
        return this.positions;
    }

    public void setPositions(String[] positions) {
        this.positions = positions;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int[] getIs_leader_in_dept() {
        return this.is_leader_in_dept;
    }

    public void setIs_leader_in_dept(int[] is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

    public int getEnable() {
        return this.enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getInitpwd() {
        return this.initpwd;
    }

    public void setInitpwd(String initpwd) {
        this.initpwd = initpwd;
    }

    public int getIsnotify() {
        return this.isnotify;
    }

    public void setIsnotify(int isnotify) {
        this.isnotify = isnotify;
    }

    public String getAvatar_mediaid() {
        return this.avatar_mediaid;
    }

    public void setAvatar_mediaid(String avatar_mediaid) {
        this.avatar_mediaid = avatar_mediaid;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Extattr getExtattr() {
        return this.extattr;
    }

    public void setExtattr(Extattr extattr) {
        this.extattr = extattr;
    }

    /// 扩展对象
    private TpDepartment currentDepartment;

    public TpDepartment getCurrentDepartment() {
        return currentDepartment;
    }

    public void setCurrentDepartment(TpDepartment currentDepartment) {
        this.currentDepartment = currentDepartment;
    }
}