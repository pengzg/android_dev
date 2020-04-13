package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/1/5.
 */

public class StatisticListBean extends BaseBean {
    private List<StatisticListBean> listData;
    private String addTime;//添加时间
    private String center_shopiId_nameref;//中心仓名字
    private String orderId;//订单id
    private String avatar;//用户头像
    private String settlementAmount;//订单金额
    private String orderStatus_nameref;//订单状态
    private String week;//星期几
    private String shopName;//店铺名

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<StatisticListBean> getListData() {
        return listData;
    }

    public void setListData(List<StatisticListBean> listData) {
        this.listData = listData;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCenter_shopiId_nameref() {
        return center_shopiId_nameref;
    }

    public void setCenter_shopiId_nameref(String center_shopiId_nameref) {
        this.center_shopiId_nameref = center_shopiId_nameref;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getOrderStatus_nameref() {
        return orderStatus_nameref;
    }

    public void setOrderStatus_nameref(String orderStatus_nameref) {
        this.orderStatus_nameref = orderStatus_nameref;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
