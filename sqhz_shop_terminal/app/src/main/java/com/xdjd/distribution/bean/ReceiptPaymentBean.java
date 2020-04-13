package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lijipei on 2017/6/11.
 */

public class ReceiptPaymentBean extends BaseBean implements Serializable{

    private List<ReceiptPaymentBean> listData;//	List集合数据

    private String gc_customerid;//	客户id
    private String gc_customerid_nameref;//	客户名称
    private String gcrr_total_amount;//	金额
    private String gcrr_discount_amount;//	优惠金额

    private String contact_name = "";
    private String contact_mobile = "";
    private String address = "";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public List<ReceiptPaymentBean> getListData() {
        return listData;
    }

    public void setListData(List<ReceiptPaymentBean> listData) {
        this.listData = listData;
    }

    public String getGc_customerid() {
        return gc_customerid;
    }

    public void setGc_customerid(String gc_customerid) {
        this.gc_customerid = gc_customerid;
    }

    public String getGc_customerid_nameref() {
        return gc_customerid_nameref;
    }

    public void setGc_customerid_nameref(String gc_customerid_nameref) {
        this.gc_customerid_nameref = gc_customerid_nameref;
    }

    public String getGcrr_total_amount() {
        return gcrr_total_amount;
    }

    public void setGcrr_total_amount(String gcrr_total_amount) {
        this.gcrr_total_amount = gcrr_total_amount;
    }

    public String getGcrr_discount_amount() {
        return gcrr_discount_amount;
    }

    public void setGcrr_discount_amount(String gcrr_discount_amount) {
        this.gcrr_discount_amount = gcrr_discount_amount;
    }
}
