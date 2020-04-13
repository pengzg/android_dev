package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/3/1.
 */

public class integralOrderBean extends BaseBean {
    private List<integralOrderBean> listData;
    private String goodsTittle;//商品标题
    private String orderStatus;//订单状态
    private String orderTime;//订单时间
    private String goods_amount;//订单积分数

    public List<integralOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<integralOrderBean> listData) {
        this.listData = listData;
    }

    public String getGoodsTittle() {
        return goodsTittle;
    }

    public void setGoodsTittle(String goodsTittle) {
        this.goodsTittle = goodsTittle;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }
}
