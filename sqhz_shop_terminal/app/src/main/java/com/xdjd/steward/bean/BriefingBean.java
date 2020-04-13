package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BriefingBean extends BaseBean {
    private float totalAmount = 0.00f;//	金额
    private String orderNum = "0";//	订单数
    private float receiveAmount = 0.00f;//	收款金额

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public float getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(float receiveAmount) {
        this.receiveAmount = receiveAmount;
    }
}
