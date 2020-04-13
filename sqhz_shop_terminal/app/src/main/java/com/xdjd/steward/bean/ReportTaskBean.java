package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * Created by lijipei on 2017/7/16.
 */

public class ReportTaskBean extends BaseBean {

    private List<ReportTaskBean> dataList;//	客户列表

    private String bl_name;//	路线名称
    private String clct_arrivetime;//	到达时间
    private String clct_customerid_address;//	地址
    private String clct_customerid_mobile;//	电话
    private String clct_customerid_nameref;//	店铺名
    private String clct_leavetime;//	离开时间
    private String clct_location_distance_desc;//	距离
    private String clct_traveltime;//	在途时间
    private String service_time;//	服务时间
    private String contact_name;//	联系人名
    private String contact_mobile;//	电话
    private String clct_userid_nameref = "";// 拜访明细中的业务员名
    private String clct_longitude;//	经度
    private String clct_latitude;//	纬度

    private List<String> imageList;//图片地址列表


    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getClct_longitude() {
        return clct_longitude;
    }

    public void setClct_longitude(String clct_longitude) {
        this.clct_longitude = clct_longitude;
    }

    public String getClct_latitude() {
        return clct_latitude;
    }

    public void setClct_latitude(String clct_latitude) {
        this.clct_latitude = clct_latitude;
    }

    public String getClct_userid_nameref() {
        return clct_userid_nameref;
    }

    public void setClct_userid_nameref(String clct_userid_nameref) {
        this.clct_userid_nameref = clct_userid_nameref;
    }

    public List<ReportTaskBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ReportTaskBean> dataList) {
        this.dataList = dataList;
    }

    public String getBl_name() {
        return bl_name;
    }

    public void setBl_name(String bl_name) {
        this.bl_name = bl_name;
    }

    public String getClct_arrivetime() {
        return clct_arrivetime;
    }

    public void setClct_arrivetime(String clct_arrivetime) {
        this.clct_arrivetime = clct_arrivetime;
    }

    public String getClct_customerid_address() {
        return clct_customerid_address;
    }

    public void setClct_customerid_address(String clct_customerid_address) {
        this.clct_customerid_address = clct_customerid_address;
    }

    public String getClct_customerid_mobile() {
        return clct_customerid_mobile;
    }

    public void setClct_customerid_mobile(String clct_customerid_mobile) {
        this.clct_customerid_mobile = clct_customerid_mobile;
    }

    public String getClct_customerid_nameref() {
        return clct_customerid_nameref;
    }

    public void setClct_customerid_nameref(String clct_customerid_nameref) {
        this.clct_customerid_nameref = clct_customerid_nameref;
    }

    public String getClct_leavetime() {
        return clct_leavetime;
    }

    public void setClct_leavetime(String clct_leavetime) {
        this.clct_leavetime = clct_leavetime;
    }

    public String getClct_location_distance_desc() {
        return clct_location_distance_desc;
    }

    public void setClct_location_distance_desc(String clct_location_distance_desc) {
        this.clct_location_distance_desc = clct_location_distance_desc;
    }

    public String getClct_traveltime() {
        return clct_traveltime;
    }

    public void setClct_traveltime(String clct_traveltime) {
        this.clct_traveltime = clct_traveltime;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
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
}
