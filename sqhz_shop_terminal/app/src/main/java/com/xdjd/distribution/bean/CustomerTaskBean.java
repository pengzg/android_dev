package com.xdjd.distribution.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/1
 *     desc   : 客户任务bean
 *     version: 1.0
 * </pre>
 */

public class CustomerTaskBean extends BaseBean implements Serializable{

    private String cc_name;//	客户名
    private String cc_id;//	客户id
    private String cc_code;//	编码
    private String cc_islocation;//	是否定位	 Y已经定位 N没有定位
    private String cc_address = "";//	地址
    private String cc_longitude;//	经度
    private String cc_latitude;//	纬度
    private String distance;//	距离 	返回单位是米 所以前台转
    private String cc_image;//	店铺图
    private String cc_contacts_mobile = "";//	电话
    private String cc_contacts_name = "";//	联系人名字

    private String cc_salesmanid_nameref;//	业务员
    private String cc_salesmanid;//	业务员id

    private String delivery_date = "";

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getCc_salesmanid_nameref() {
        return cc_salesmanid_nameref;
    }

    public void setCc_salesmanid_nameref(String cc_salesmanid_nameref) {
        this.cc_salesmanid_nameref = cc_salesmanid_nameref;
    }

    public String getCc_salesmanid() {
        return cc_salesmanid;
    }

    public void setCc_salesmanid(String cc_salesmanid) {
        this.cc_salesmanid = cc_salesmanid;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getCc_islocation() {
        return cc_islocation;
    }

    public void setCc_islocation(String cc_islocation) {
        this.cc_islocation = cc_islocation;
    }

    public String getCc_address() {
        return cc_address;
    }

    public void setCc_address(String cc_address) {
        this.cc_address = cc_address;
    }

    public String getCc_longitude() {
        return cc_longitude;
    }

    public void setCc_longitude(String cc_longitude) {
        this.cc_longitude = cc_longitude;
    }

    public String getCc_latitude() {
        return cc_latitude;
    }

    public void setCc_latitude(String cc_latitude) {
        this.cc_latitude = cc_latitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCc_image() {
        return cc_image;
    }

    public void setCc_image(String cc_image) {
        this.cc_image = cc_image;
    }

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contacts_name) {
        this.cc_contacts_name = cc_contacts_name;
    }
}
