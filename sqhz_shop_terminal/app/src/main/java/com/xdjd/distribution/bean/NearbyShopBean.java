package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/9/22.
 */

public class NearbyShopBean extends BaseBean implements Serializable{
    private String ccl_addtime;//采集时间
    private String ccl_id;//客户id
    private String ccl_contacts_mobile;//联系电话
    private String ccl_contacts_name;//联系人姓名
    private String ccl_img;//店铺图像
    private String ccl_latitude;//纬度
    private String ccl_longitude;//经度
    private String distance;//距离
    private String ccl_name;//店铺名
    private  String ccl_address;//地址
    private List<NearbyShopBean> dataList;

    public String getCcl_addtime() {
        return ccl_addtime;
    }

    public void setCcl_addtime(String ccl_addtime) {
        this.ccl_addtime = ccl_addtime;
    }

    public String getCcl_id() {
        return ccl_id;
    }

    public void setCcl_id(String ccl_id) {
        this.ccl_id = ccl_id;
    }

    public String getCcl_contacts_mobile() {
        return ccl_contacts_mobile;
    }

    public void setCcl_contacts_mobile(String ccl_contacts_mobile) {
        this.ccl_contacts_mobile = ccl_contacts_mobile;
    }

    public String getCcl_contacts_name() {
        return ccl_contacts_name;
    }

    public void setCcl_contacts_name(String ccl_contacts_name) {
        this.ccl_contacts_name = ccl_contacts_name;
    }

    public String getCcl_img() {
        return ccl_img;
    }

    public void setCcl_img(String ccl_img) {
        this.ccl_img = ccl_img;
    }

    public String getCcl_latitude() {
        return ccl_latitude;
    }

    public void setCcl_latitude(String ccl_latitude) {
        this.ccl_latitude = ccl_latitude;
    }

    public String getCcl_longitude() {
        return ccl_longitude;
    }

    public void setCcl_longitude(String ccl_longitude) {
        this.ccl_longitude = ccl_longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCcl_name() {
        return ccl_name;
    }

    public void setCcl_name(String ccl_name) {
        this.ccl_name = ccl_name;
    }

    public String getCcl_address() {
        return ccl_address;
    }

    public void setCcl_address(String ccl_address) {
        this.ccl_address = ccl_address;
    }

    public List<NearbyShopBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<NearbyShopBean> dataList) {
        this.dataList = dataList;
    }
}

