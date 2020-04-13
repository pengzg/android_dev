package com.xdjd.storebox.bean;

/**
 * Created by freestyle_hong on 2017/1/3.
 */

public class RegisterInfoBean extends BaseBean {
    private String userId;//用户编码
    private int checkStatus;//审核状态
    private String nickName;//注册姓名
    private String shopName;//超市名称
    private int shopId;//所在地区id
    private String centerShopName;//地区
    private String address;//详细地址
    private String spareTel;//备用电话

    //注册成功后,等待审核返回的参数值
    private String user;//用户id
    private String flag;//	跳转标识	1:首页2审核
    private String center_shopid;//	中心仓id
    private String spreadMobile;//	推广员电话
    private String spread_name;//	推广员姓名
    //private String centerShopName;//	中心仓店铺名
    private String shopTel;//	中心仓电话
    private String unionId;//	用户绑定微信的标志	有值为绑定

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCenter_shopid() {
        return center_shopid;
    }

    public void setCenter_shopid(String center_shopid) {
        this.center_shopid = center_shopid;
    }

    public String getSpreadMobile() {
        return spreadMobile;
    }

    public void setSpreadMobile(String spreadMobile) {
        this.spreadMobile = spreadMobile;
    }

    public String getSpread_name() {
        return spread_name;
    }

    public void setSpread_name(String spread_name) {
        this.spread_name = spread_name;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getCenterShopName() {
        return centerShopName;
    }

    public void setCenterShopName(String centerShopName) {
        this.centerShopName = centerShopName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpareTel() {
        return spareTel;
    }

    public void setSpareTel(String spareTel) {
        this.spareTel = spareTel;
    }
}
