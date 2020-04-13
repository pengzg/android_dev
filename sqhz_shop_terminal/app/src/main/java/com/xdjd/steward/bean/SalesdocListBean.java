package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * Created by lijipei on 2017/7/13.
 */

public class SalesdocListBean extends BaseBean {

    private List<SalesdocListBean> dataList;//	商品列表

    private String su_id;//	业务员id
    private String su_name;//	业务员姓名

    public List<SalesdocListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SalesdocListBean> dataList) {
        this.dataList = dataList;
    }

    public String getSu_id() {
        return su_id;
    }

    public void setSu_id(String su_id) {
        this.su_id = su_id;
    }

    public String getSu_name() {
        return su_name;
    }

    public void setSu_name(String su_name) {
        this.su_name = su_name;
    }
}
