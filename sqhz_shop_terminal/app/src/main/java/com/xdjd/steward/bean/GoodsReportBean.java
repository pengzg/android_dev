package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsReportBean extends BaseBean {

    private List<GoodsReportBean> dataList;//	列表

    private String goodsName = "";//	商品名
    private float amount;//	销售金额

    public List<GoodsReportBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<GoodsReportBean> dataList) {
        this.dataList = dataList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
