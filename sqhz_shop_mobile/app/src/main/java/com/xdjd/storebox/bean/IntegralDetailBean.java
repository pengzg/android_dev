package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/2/28.
 */

public class IntegralDetailBean extends BaseBean {
    private List<IntegralDetailBean> listData;
    private String wmad_id;
    private String wmad_business_name;//业务描述
    private String wmad_change_account;//积分变化量
    private String wmad_add_time;//时间

    public List<IntegralDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<IntegralDetailBean> listData) {
        this.listData = listData;
    }

    public String getWmad_id() {
        return wmad_id;
    }

    public void setWmad_id(String wmad_id) {
        this.wmad_id = wmad_id;
    }

    public String getWmad_business_name() {
        return wmad_business_name;
    }

    public void setWmad_business_name(String wmad_business_name) {
        this.wmad_business_name = wmad_business_name;
    }

    public String getWmad_change_account() {
        return wmad_change_account;
    }

    public void setWmad_change_account(String wmad_change_account) {
        this.wmad_change_account = wmad_change_account;
    }

    public String getWmad_add_time() {
        return wmad_add_time;
    }

    public void setWmad_add_time(String wmad_add_time) {
        this.wmad_add_time = wmad_add_time;
    }
}
