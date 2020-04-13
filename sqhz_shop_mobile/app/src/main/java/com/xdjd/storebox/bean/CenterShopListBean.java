package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 中心仓地址列表bean
 * Created by lijipei on 2017/3/14.
 */

public class CenterShopListBean extends BaseBean {

    private List<CenterShopListBean> dataList;//	中心仓列表

    private String distance;//	距离
    private String ms_name;//	名字
    private String ms_id;//	编码
    private String ms_address;//	地址

    public String getMs_address() {
        return ms_address;
    }

    public void setMs_address(String ms_address) {
        this.ms_address = ms_address;
    }

    public List<CenterShopListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CenterShopListBean> dataList) {
        this.dataList = dataList;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMs_name() {
        return ms_name;
    }

    public void setMs_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public String getMs_id() {
        return ms_id;
    }

    public void setMs_id(String ms_id) {
        this.ms_id = ms_id;
    }
}
