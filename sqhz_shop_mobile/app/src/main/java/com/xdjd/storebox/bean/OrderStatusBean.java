package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class OrderStatusBean extends BaseBean {
    private String wol_progress_time;//时间
    private String wol_progress_type_nameref;//状态
    private String wol_progress_type;//状态值
    private String wol_progress_explain;//状态描述
    List<OrderStatusBean> listData;

    public String getWol_progress_time() {
        return wol_progress_time;
    }

    public void setWol_progress_time(String wol_progress_time) {
        this.wol_progress_time = wol_progress_time;
    }

    public String getWol_progress_type_nameref() {
        return wol_progress_type_nameref;
    }

    public void setWol_progress_type_nameref(String wol_progress_type_nameref) {
        this.wol_progress_type_nameref = wol_progress_type_nameref;
    }

    public String getWol_progress_type() {
        return wol_progress_type;
    }

    public void setWol_progress_type(String wol_progress_type) {
        this.wol_progress_type = wol_progress_type;
    }

    public String getWol_progress_explain() {
        return wol_progress_explain;
    }

    public void setWol_progress_explain(String wol_progress_explain) {
        this.wol_progress_explain = wol_progress_explain;
    }

    public List<OrderStatusBean> getListData() {
        return listData;
    }

    public void setListData(List<OrderStatusBean> listData) {
        this.listData = listData;
    }
}
