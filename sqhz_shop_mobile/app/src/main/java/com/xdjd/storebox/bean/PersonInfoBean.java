package com.xdjd.storebox.bean;

/**
 * Created by freestyle_hong on 2016/12/26.
 */

public class PersonInfoBean extends BaseBean {
    private String cc_contacts_name;//昵称
    private String birthDay;//生日
    private String cc_contacts_mobile;//手机
    private String cc_name;//店铺名
    private String shopTel;//客服电话
    private String cc_image;//个人图像
    private String unionId;//绑定微信是否
    private String userType;//用户类型
    private String cc_openid;//绑定微信是否


    public String getCc_openid() {
        return cc_openid;
    }

    public void setCc_openid(String cc_openid) {
        this.cc_openid = cc_openid;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
    public String getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contacts_name) {
        this.cc_contacts_name = cc_contacts_name;
    }

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getCc_image() {
        return cc_image;
    }

    public void setCc_image(String cc_image) {
        this.cc_image = cc_image;
    }
}
