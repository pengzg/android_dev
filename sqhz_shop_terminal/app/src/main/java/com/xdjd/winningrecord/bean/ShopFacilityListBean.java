package com.xdjd.winningrecord.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/21
 *     desc   : 店铺绑定设备列表bean
 *     version: 1.0
 * </pre>
 */

public class ShopFacilityListBean extends BaseBean {

    private List<ShopFacilityListBean> listData;//	已关联设备的店铺列表

    private String ms_id;//	店铺id
    private String ms_img;//	店铺图像
    private String ms_name;//	店铺名称
    private String ms_mobile;//	店铺手机号
    private String ms_address;//	店铺地址
    private float ms_longitude;//	经度
    private float ms_latitude;//	维度

    public List<ShopFacilityListBean> getListData() {
        return listData;
    }

    public void setListData(List<ShopFacilityListBean> listData) {
        this.listData = listData;
    }

    public String getMs_id() {
        return ms_id;
    }

    public void setMs_id(String ms_id) {
        this.ms_id = ms_id;
    }

    public String getMs_img() {
        return ms_img;
    }

    public void setMs_img(String ms_img) {
        this.ms_img = ms_img;
    }

    public String getMs_name() {
        return ms_name;
    }

    public void setMs_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public String getMs_mobile() {
        return ms_mobile;
    }

    public void setMs_mobile(String ms_mobile) {
        this.ms_mobile = ms_mobile;
    }

    public String getMs_address() {
        return ms_address;
    }

    public void setMs_address(String ms_address) {
        this.ms_address = ms_address;
    }

    public float getMs_longitude() {
        return ms_longitude;
    }

    public void setMs_longitude(float ms_longitude) {
        this.ms_longitude = ms_longitude;
    }

    public float getMs_latitude() {
        return ms_latitude;
    }

    public void setMs_latitude(float ms_latitude) {
        this.ms_latitude = ms_latitude;
    }
}
