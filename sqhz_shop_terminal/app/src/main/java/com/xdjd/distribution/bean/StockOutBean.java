package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijipei on 2017/6/6.
 */

public class StockOutBean extends BaseBean implements Serializable{

    private List<StockOutBean> dataList;//	商品列表
    private String eim_customerid;//	客户id
    private String eim_customerid_nameref = "";//	客户名
    private String eim_id;//	单据id
    private String eim_code = "";//	编码
    private String eim_operator;//	操作人id
    private String eim_operator_nameref;//	操作人姓名
    private String eim_billdate;//	单据日期

    private String contact_name = "";//客户姓名
    private String contact_mobile = "";//客户电话
    private String customer_address;

    private String eim_totalamount;//价格

    private String eim_salesid;//	业务员id
    private String eim_salesid_nameref;//	业务员名

    private String eim_stats_nameref = "";//订单状态
    private String eim_source_id;//	来源id
    private String eim_source_code;//	来源code


    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
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

    public String getEim_stats_nameref() {
        return eim_stats_nameref;
    }

    public void setEim_stats_nameref(String eim_stats_nameref) {
        this.eim_stats_nameref = eim_stats_nameref;
    }

    public String getEim_code() {
        return eim_code;
    }

    public void setEim_code(String eim_code) {
        this.eim_code = eim_code;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
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

    public String getEim_totalamount() {
        return eim_totalamount;
    }

    public void setEim_totalamount(String eim_totalamount) {
        this.eim_totalamount = eim_totalamount;
    }

    public List<StockOutBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<StockOutBean> dataList) {
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

    public String getEim_operator() {
        return eim_operator;
    }

    public void setEim_operator(String eim_operator) {
        this.eim_operator = eim_operator;
    }

    public String getEim_operator_nameref() {
        return eim_operator_nameref;
    }

    public void setEim_operator_nameref(String eim_operator_nameref) {
        this.eim_operator_nameref = eim_operator_nameref;
    }

    public String getEim_billdate() {
        return eim_billdate;
    }

    public void setEim_billdate(String eim_billdate) {
        this.eim_billdate = eim_billdate;
    }
}
