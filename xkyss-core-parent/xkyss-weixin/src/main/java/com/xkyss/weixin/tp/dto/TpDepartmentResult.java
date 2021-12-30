package com.xkyss.weixin.tp.dto;

import com.xkyss.weixin.tp.bean.TpDepartment;
import com.xkyss.weixin.tp.error.TpError;

import java.util.List;

public class TpDepartmentResult extends TpError {
    private List<TpDepartment> department;

    public List<TpDepartment> getDepartment() {
        return department;
    }

    public void setDepartment(List<TpDepartment> department) {
        this.department = department;
    }
}
