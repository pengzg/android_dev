package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PrintGlCashBean extends BaseBean {


    private String cashtype;//	类型
    private String customerid_nameref;//	客户姓名
    private String billdate;//时间
    private List<PrintGlCashBean> dataList;//	订单商品列表

    private String bi_name;//	名字
    private String gcd_amount;//	金额

    //----------------
    private Integer source;
    private String source_code;
    private String stats;//订单状态--6:已发货收款


    private String xjAmount = "0.00";//	现金
    private String yskAmount = "0.00";//	预收款
    private String skAmount = "0.00";//	刷卡
    private String yhAmount = "0.00";//	优惠
    private String ysAmount = "0.00";//	应收
    private String sfkAmount = "0.00";//	实付款


    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSource_code() {
        return source_code;
    }

    public void setSource_code(String source_code) {
        this.source_code = source_code;
    }

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getYskAmount() {
        return yskAmount;
    }

    public void setYskAmount(String yskAmount) {
        this.yskAmount = yskAmount;
    }

    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public String getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(String yhAmount) {
        this.yhAmount = yhAmount;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getSfkAmount() {
        return sfkAmount;
    }

    public void setSfkAmount(String sfkAmount) {
        this.sfkAmount = sfkAmount;
    }

    public String getBilldate() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate = billdate;
    }

    public String getCashtype() {
        return cashtype;
    }

    public void setCashtype(String cashtype) {
        this.cashtype = cashtype;
    }

    public String getCustomerid_nameref() {
        return customerid_nameref;
    }

    public void setCustomerid_nameref(String customerid_nameref) {
        this.customerid_nameref = customerid_nameref;
    }

    public List<PrintGlCashBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PrintGlCashBean> dataList) {
        this.dataList = dataList;
    }

    public String getBi_name() {
        return bi_name;
    }

    public void setBi_name(String bi_name) {
        this.bi_name = bi_name;
    }

    public String getGcd_amount() {
        return gcd_amount;
    }

    public void setGcd_amount(String gcd_amount) {
        this.gcd_amount = gcd_amount;
    }
}
