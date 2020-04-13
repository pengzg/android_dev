package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesLocationBean extends BaseBean {

    private List<SalesLocationBean> listData;//	商品列表

    private String clct_userid_nameref;//	业务员名称
    private float clct_longitude;//	经度
    private float clct_latitude;//	纬度
    private String clct_arrivetime;//	到达 时间


    public List<SalesLocationBean> getListData() {
        return listData;
    }

    public void setListData(List<SalesLocationBean> listData) {
        this.listData = listData;
    }

    public String getClct_userid_nameref() {
        return clct_userid_nameref;
    }

    public void setClct_userid_nameref(String clct_userid_nameref) {
        this.clct_userid_nameref = clct_userid_nameref;
    }

    public float getClct_longitude() {
        return clct_longitude;
    }

    public void setClct_longitude(float clct_longitude) {
        this.clct_longitude = clct_longitude;
    }

    public float getClct_latitude() {
        return clct_latitude;
    }

    public void setClct_latitude(float clct_latitude) {
        this.clct_latitude = clct_latitude;
    }

    public String getClct_arrivetime() {
        return clct_arrivetime;
    }

    public void setClct_arrivetime(String clct_arrivetime) {
        this.clct_arrivetime = clct_arrivetime;
    }
}
