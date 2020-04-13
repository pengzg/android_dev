package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesmanBean extends BaseBean {

    private List<SalesmanBean> dataList;//	列表

    private String om_salesdocid;//	业务员id
    private String om_salesdocid_nameref = "";//	业务员姓名
    private float amount = 0;//	销售金额

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public List<SalesmanBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SalesmanBean> dataList) {
        this.dataList = dataList;
    }

    public String getOm_salesdocid_nameref() {
        return om_salesdocid_nameref;
    }

    public void setOm_salesdocid_nameref(String om_salesdocid_nameref) {
        this.om_salesdocid_nameref = om_salesdocid_nameref;
    }

    public String getOm_salesdocid() {
        return om_salesdocid;
    }

    public void setOm_salesdocid(String om_salesdocid) {
        this.om_salesdocid = om_salesdocid;
    }
}
