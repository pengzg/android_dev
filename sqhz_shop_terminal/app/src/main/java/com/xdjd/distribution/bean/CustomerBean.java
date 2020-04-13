package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/4
 *     desc   : 顾客bean
 *     version: 1.0
 * </pre>
 */

public class CustomerBean extends BaseBean {

    private List<CustomerBean> dataList;//	客户列表

    private String cc_name;//	店铺名
    private String cc_id;//	客户id
    private String cc_code;//	编码
    private String cc_islocation;//	是否定位	 Y已经定位 N没有定位
    private String cc_address;//	地址
    private String cc_longitude;//	经度
    private String cc_latitude;//	纬度
    private String cc_contacts_mobile;//	联系电话
    private String cc_contacts_name;//	联系人
    private String cc_image;//店铺图像
    private String cc_checkstats;//	审核状态	 1.提交待审核  2.审核通过  3.审核不通过

    public String getCc_image() {
        return cc_image;
    }

    public void setCc_image(String cc_image) {
        this.cc_image = cc_image;
    }

    public String getCc_checkstats() {
        return cc_checkstats;
    }

    public void setCc_checkstats(String cc_checkstats) {
        this.cc_checkstats = cc_checkstats;
    }

    public List<CustomerBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CustomerBean> dataList) {
        this.dataList = dataList;
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

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contact_name) {
        this.cc_contacts_name = cc_contact_name;
    }
}
