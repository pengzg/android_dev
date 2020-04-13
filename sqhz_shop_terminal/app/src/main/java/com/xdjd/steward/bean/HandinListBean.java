package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * Created by lijipei on 2017/7/16.
 */

public class HandinListBean extends BaseBean {

    private List<HandinListBean> dataList;//	商品列表
    private float amount;//	金额
    private String gc_salesid_nameref;//	业务员姓名

    public List<HandinListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<HandinListBean> dataList) {
        this.dataList = dataList;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getGc_salesid_nameref() {
        return gc_salesid_nameref;
    }

    public void setGc_salesid_nameref(String gc_salesid_nameref) {
        this.gc_salesid_nameref = gc_salesid_nameref;
    }
}
