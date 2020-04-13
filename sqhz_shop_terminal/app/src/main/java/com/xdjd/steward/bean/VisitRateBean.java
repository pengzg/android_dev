package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitRateBean extends BaseBean {

    private String orderCustNum = "";//	下单总数

    private List<VisitRateBean> listData;//	商品列表

    private String salesid = ""; //业务员id
    private String ordernum = "0";//	订单数
    private String ratio = "0";//	达成率
    private String salesid_nameref = "";//	业务员名
    private String visitNum = "0";//	拜访数

    private String visitDate = "";//	日期

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getOrderCustNum() {
        return orderCustNum;
    }

    public void setOrderCustNum(String orderCustNum) {
        this.orderCustNum = orderCustNum;
    }

    public String getSalesid() {
        return salesid;
    }

    public void setSalesid(String salesid) {
        this.salesid = salesid;
    }

    public List<VisitRateBean> getListData() {
        return listData;
    }

    public void setListData(List<VisitRateBean> listData) {
        this.listData = listData;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getSalesid_nameref() {
        return salesid_nameref;
    }

    public void setSalesid_nameref(String salesid_nameref) {
        this.salesid_nameref = salesid_nameref;
    }

    public String getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(String visitNum) {
        this.visitNum = visitNum;
    }
}
