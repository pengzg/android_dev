package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DisplayListBean extends BaseBean implements Serializable {

    private String num;//
    private String totalAmount;//
    private List<DisplayListBean> dataList;//	商品列表

    private String eim_customerid;//	客户id
    private String eim_customerid_nameref;//	客户名
    private String eim_id;//	单据id
    private String eim_billdate;//	单据日期
    private String eim_totalamount;//	单据总金额
    private String eim_salesid;//	业务员id
    private String eim_salesid_nameref;//	业务员名
    private String eim_code;//	编码
    private String eim_stats_nameref;//	状态
    private String eim_source;//	来源 1订单
    private String eim_source_id;//	来源id
    private String eim_source_code;//	来源code

    private String contact_name;
    private String customer_address;
    private String contact_mobile;


    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<DisplayListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DisplayListBean> dataList) {
        this.dataList = dataList;
    }

    public String getEim_customerid() {
        return eim_customerid;
    }

    public void setEim_customerid(String eim_customerid) {
        this.eim_customerid = eim_customerid;
    }

    public String getEim_customerid_nameref() {
        return eim_customerid_nameref;
    }

    public void setEim_customerid_nameref(String eim_customerid_nameref) {
        this.eim_customerid_nameref = eim_customerid_nameref;
    }

    public String getEim_id() {
        return eim_id;
    }

    public void setEim_id(String eim_id) {
        this.eim_id = eim_id;
    }

    public String getEim_billdate() {
        return eim_billdate;
    }

    public void setEim_billdate(String eim_billdate) {
        this.eim_billdate = eim_billdate;
    }

    public String getEim_totalamount() {
        return eim_totalamount;
    }

    public void setEim_totalamount(String eim_totalamount) {
        this.eim_totalamount = eim_totalamount;
    }

    public String getEim_salesid() {
        return eim_salesid;
    }

    public void setEim_salesid(String eim_salesid) {
        this.eim_salesid = eim_salesid;
    }

    public String getEim_salesid_nameref() {
        return eim_salesid_nameref;
    }

    public void setEim_salesid_nameref(String eim_salesid_nameref) {
        this.eim_salesid_nameref = eim_salesid_nameref;
    }

    public String getEim_code() {
        return eim_code;
    }

    public void setEim_code(String eim_code) {
        this.eim_code = eim_code;
    }

    public String getEim_stats_nameref() {
        return eim_stats_nameref;
    }

    public void setEim_stats_nameref(String eim_stats_nameref) {
        this.eim_stats_nameref = eim_stats_nameref;
    }

    public String getEim_source() {
        return eim_source;
    }

    public void setEim_source(String eim_source) {
        this.eim_source = eim_source;
    }

    public String getEim_source_id() {
        return eim_source_id;
    }

    public void setEim_source_id(String eim_source_id) {
        this.eim_source_id = eim_source_id;
    }

    public String getEim_source_code() {
        return eim_source_code;
    }

    public void setEim_source_code(String eim_source_code) {
        this.eim_source_code = eim_source_code;
    }
}
