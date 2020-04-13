package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/1
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RefundReportBean extends BaseBean {

    private String amount = "0.00";//	金额
    private String orderNum = "0";//	数量

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
