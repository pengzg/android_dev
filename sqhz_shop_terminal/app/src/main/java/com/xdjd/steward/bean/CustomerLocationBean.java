package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/10/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerLocationBean extends BaseBean implements Serializable{

    private List<CustomerLocationBean> dataList;//	客户列表

    private String cc_name;//	店铺名
    private String cc_id;//	客户id
    private float cc_longitude;//	经度
    private float cc_latitude;//	纬度

    public float getCc_longitude() {
        return cc_longitude;
    }

    public void setCc_longitude(float cc_longitude) {
        this.cc_longitude = cc_longitude;
    }

    public float getCc_latitude() {
        return cc_latitude;
    }

    public void setCc_latitude(float cc_latitude) {
        this.cc_latitude = cc_latitude;
    }

    public List<CustomerLocationBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CustomerLocationBean> dataList) {
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

}
