package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/8.
 */

public class ResetPwdBean extends BaseBean implements Serializable{
    private String userId;
    private String orgid;
    private String orgid_nameref;
    private String username;
    private String salesmanid;
    private String orgid_mobile;
    private String storehouseid;
    private String storehouseidName;

    private String mobilePnoe;//登录账号
    private String password;//密码
    List<ResetPwdBean> listData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getOrgid_nameref() {
        return orgid_nameref;
    }

    public void setOrgid_nameref(String orgid_nameref) {
        this.orgid_nameref = orgid_nameref;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalesmanid() {
        return salesmanid;
    }

    public void setSalesmanid(String salesmanid) {
        this.salesmanid = salesmanid;
    }

    public String getOrgid_mobile() {
        return orgid_mobile;
    }

    public void setOrgid_mobile(String orgid_mobile) {
        this.orgid_mobile = orgid_mobile;
    }

    public String getStorehouseid() {
        return storehouseid;
    }

    public void setStorehouseid(String storehouseid) {
        this.storehouseid = storehouseid;
    }

    public String getStorehouseidName() {
        return storehouseidName;
    }

    public void setStorehouseidName(String storehouseidName) {
        this.storehouseidName = storehouseidName;
    }

    public List<ResetPwdBean> getListData() {
        return listData;
    }

    public void setListData(List<ResetPwdBean> listData) {
        this.listData = listData;
    }

    public String getMobilePnoe() {
        return mobilePnoe;
    }

    public void setMobilePnoe(String mobilePnoe) {
        this.mobilePnoe = mobilePnoe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
