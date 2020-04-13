package com.xdjd.distribution.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/6/4.
 */

public class PayTypeBean extends BaseBean {

    private List<PayTypeBean> dataList;//	数据列表

    private String bd_name;//	名字
    private String bd_code;//	id

    public List<PayTypeBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PayTypeBean> dataList) {
        this.dataList = dataList;
    }

    public String getBd_name() {
        return bd_name;
    }

    public void setBd_name(String bd_name) {
        this.bd_name = bd_name;
    }

    public String getBd_code() {
        return bd_code;
    }

    public void setBd_code(String bd_code) {
        this.bd_code = bd_code;
    }
}
