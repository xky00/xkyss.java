package com.xkyss.weixin.tp.bean;

/**
 * 成员详情
 * http://192.168.0.71/api/doc#10028/3%E3%80%81%E4%BD%BF%E7%94%A8user_ticket%E8%8E%B7%E5%8F%96%E6%88%90%E5%91%98%E8%AF%A6%E6%83%85
 */
public class TpUserDetail {
    /**
     * 成员UserID
     */
    private String userid;

    /**
     * 成员姓名
     */
    private String name;

    /**
     * 成员所属部门
     */
    private Integer[] department;

    /**
     * 是否部门领导
     */
    private int[] is_leader_in_dept;

    /**
     * 职位信息
     */
    private String position;

    /**
     * 在各个部门的职位信息
     */
    private String[] positions;

    /**
     * 成员手机号，仅在用户同意snsapi_privateinfo授权时返回
     */
    private String mobile;

    /**
     * 性别。0表示未定义，1表示男性，2表示女性
     */
    private String gender;

    /**
     * 成员邮箱，仅在用户同意snsapi_privateinfo授权时返回
     */
    private String email;

    /**
     * 头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可
     */
    private String avatar;

    /**
     * 成员二维码图片地址
     */
    private String qr_code;

    /**
     * 排序
     */
    private Integer[] order;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer[] getDepartment() {
        return department;
    }

    public void setDepartment(Integer[] department) {
        this.department = department;
    }

    public int[] getIs_leader_in_dept() {
        return is_leader_in_dept;
    }

    public void setIs_leader_in_dept(int[] is_leader_in_dept) {
        this.is_leader_in_dept = is_leader_in_dept;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String[] getPositions() {
        return positions;
    }

    public void setPositions(String[] positions) {
        this.positions = positions;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public Integer[] getOrder() {
        return order;
    }

    public void setOrder(Integer[] order) {
        this.order = order;
    }
}
