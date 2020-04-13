package com.xdjd.storebox.bean;

/**
 * Created by freestyle_hong on 2017/1/4.
 */

public class PromoteStatisticBean extends BaseBean{
    private String orderCount;//订单总数
    private String spreadNum;//推广人数
    private String totalAmount;//订单总金额

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getSpreadNum() {
        return spreadNum;
    }

    public void setSpreadNum(String spreadNum) {
        this.spreadNum = spreadNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
