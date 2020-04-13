package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijipei on 2016/12/23.
 */

public class LoginBean extends BaseBean implements Serializable{
    /*private String user;
    private String spread_name;
    private String spreadMobile;
    private String centerShopName;
    private String unionId;
    private String center_shopid;//	中心仓id
    private String userShopId;//	用户店铺id
    private String userId;//快速注册时回传的用户id*/
    private String userId;//用户信息编号
    private String orgid; //公司id
    private String orgid_mobile;//公司电话
    private String username;//客户名称
    private String salesmanid;//业务员id
    private String orgid_nameref;//	公司的名称
    private String orgid_content;//公司经营内容
    private String storehouseid;//默认发货仓库id
    private String storehouseidName;//仓库名称
    private String bs_is_negative;//是否允许负库存
    private String bs_is_virtual;//是否虚拟库存
    private String mobilePhone;//登录账号
    private String password;//登录密码

    private List<LoginBean> listData;//	用户的列表数据	 如果 只有一条数据 则取第一条  否则选择列表中的一条

    public String getOrgid_content() {
        return orgid_content;
    }

    public void setOrgid_content(String orgid_content) {
        this.orgid_content = orgid_content;
    }

    public String getOrgid_mobile() {
        return orgid_mobile;
    }

    public void setOrgid_mobile(String orgid_mobile) {
        this.orgid_mobile = orgid_mobile;
    }

    public String getOrgid_nameref() {
        return orgid_nameref;
    }

    public void setOrgid_nameref(String orgid_nameref) {
        this.orgid_nameref = orgid_nameref;
    }

    public List<LoginBean> getListData() {
        return listData;
    }

    public void setListData(List<LoginBean> listData) {
        this.listData = listData;
    }

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

    public String getBs_is_negative() {
        return bs_is_negative;
    }

    public void setBs_is_negative(String bs_is_negative) {
        this.bs_is_negative = bs_is_negative;
    }

    public String getBs_is_virtual() {
        return bs_is_virtual;
    }

    public void setBs_is_virtual(String bs_is_virtual) {
        this.bs_is_virtual = bs_is_virtual;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
