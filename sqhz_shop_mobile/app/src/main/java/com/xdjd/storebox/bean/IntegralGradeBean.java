package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/3/2.
 */

public class IntegralGradeBean extends BaseBean {
    private String 	priceId;//价格id
    private String 	priceDesc;//价格描述
    private List<IntegralGradeBean>listData;

    public List<IntegralGradeBean> getListData() {
        return listData;
    }

    public void setListData(List<IntegralGradeBean> listData) {
        this.listData = listData;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public String getPriceDesc() {
        return priceDesc;
    }

    public void setPriceDesc(String priceDesc) {
        this.priceDesc = priceDesc;
    }
}
