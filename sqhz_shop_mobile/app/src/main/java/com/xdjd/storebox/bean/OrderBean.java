package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2016/12/12.
 */

public class OrderBean extends BaseBean {
    private String paymengNums;//待付款订单数
    private String deliverNums;//发货订单数
    private String finishNums;//完成订单数
    private String refundNums;//退货订单数

    List<OrderListBean>list;

    public List<OrderListBean> getList() {
        return list;
    }

    public void setList(List<OrderListBean> list) {
        this.list = list;
    }

    public String getPaymengNums() {
        return paymengNums;
    }

    public void setPaymengNums(String paymengNums) {
        this.paymengNums = paymengNums;
    }

    public String getDeliverNums() {
        return deliverNums;
    }

    public void setDeliverNums(String deliverNums) {
        this.deliverNums = deliverNums;
    }

    public String getFinishNums() {
        return finishNums;
    }

    public void setFinishNums(String finishNums) {
        this.finishNums = finishNums;
    }

    public String getRefundNums() {
        return refundNums;
    }

    public void setRefundNums(String refundNums) {
        this.refundNums = refundNums;
    }




}
