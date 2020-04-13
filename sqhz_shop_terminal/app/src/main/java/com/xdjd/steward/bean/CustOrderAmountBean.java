package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustOrderAmountBean extends BaseBean {

    private List<CustOrderAmountBean> listData;//	商品列表

    private float amount;//	总金额
    private String customerId;//	客户id
    private String customerId_nameref;//	客户名
    private String orderNum;//	订单数量

    public List<CustOrderAmountBean> getListData() {
        return listData;
    }

    public void setListData(List<CustOrderAmountBean> listData) {
        this.listData = listData;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId_nameref() {
        return customerId_nameref;
    }

    public void setCustomerId_nameref(String customerId_nameref) {
        this.customerId_nameref = customerId_nameref;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
