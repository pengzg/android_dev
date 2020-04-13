package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/1/4.
 */

public class PromoteUserBean extends BaseBean {
    private List<PromoteUserBean> dataList;
    private String address;//地址
    private String avatar;//图像
    private String mobile;//电话
    private String nickname;//昵称
    private String orderCount;//订单总笔数
    private String shopName;//店铺名称
    private String spreadTime;//推广时间
    private String totalAmount;//订单总金额

    public List<PromoteUserBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PromoteUserBean> dataList) {
        this.dataList = dataList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSpreadTime() {
        return spreadTime;
    }

    public void setSpreadTime(String spreadTime) {
        this.spreadTime = spreadTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
