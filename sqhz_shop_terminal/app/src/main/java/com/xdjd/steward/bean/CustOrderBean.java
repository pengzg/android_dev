package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/1
 *     desc   : 客户订单统计bean
 *     version: 1.0
 * </pre>
 */

public class CustOrderBean extends BaseBean {

    private List<CustOrderBean> dataList;//	客户订单列表

    private float amount = 0;//	金额
    private String categoryid_nameref  = "";//	类别名
    private String areaid_nameref = "";//	区域名

    public List<CustOrderBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CustOrderBean> dataList) {
        this.dataList = dataList;
    }

    public String getAreaid_nameref() {
        return areaid_nameref;
    }

    public void setAreaid_nameref(String areaid_nameref) {
        this.areaid_nameref = areaid_nameref;
    }

    public String getCategoryid_nameref() {
        return categoryid_nameref;
    }

    public void setCategoryid_nameref(String categoryid_nameref) {
        this.categoryid_nameref = categoryid_nameref;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
