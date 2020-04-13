package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PrintListBean extends BaseBean implements Serializable{

    private List<PrintListBean> dataList;//	订单商品列表

    private String id;//	id
    private String code;//	编码
    private String type;//	类型  	1订单  2.收付款 3出入库
    private String customerid_nameref;//	客户名
    private String stats;//	状态
    private String date;//	时间
    private String stats_nameref;//	状态
    private String amount;//	金额
    private int orderType;//	订单类型
    private String source;//	来源	2 订单申报  3.车销
    private String source_nameref;//	来源

    private String contact_name = "";
    private String contact_mobile = "";
    private String contact_address = "";//	地址

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_nameref() {
        return source_nameref;
    }

    public void setSource_nameref(String source_nameref) {
        this.source_nameref = source_nameref;
    }

    public String getStats_nameref() {
        return stats_nameref;
    }

    public void setStats_nameref(String stats_nameref) {
        this.stats_nameref = stats_nameref;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public List<PrintListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PrintListBean> dataList) {
        this.dataList = dataList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCustomerid_nameref() {
        return customerid_nameref;
    }

    public void setCustomerid_nameref(String customerid_nameref) {
        this.customerid_nameref = customerid_nameref;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
