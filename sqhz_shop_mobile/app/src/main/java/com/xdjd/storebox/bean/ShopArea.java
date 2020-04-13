package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class ShopArea {

    private String ms_id;
    private String ms_title;
    private String repCode;
    private String repMsg;
    private List<ShopArea> listData;

    public List<ShopArea> getListData() {
        return listData;
    }

    public void setListData(List<ShopArea> listData) {
        this.listData = listData;
    }

    public String getMs_title() {
        return ms_title;
    }

    public void setMs_title(String ms_title) {
        this.ms_title = ms_title;
    }

    public String getMs_id() {
        return ms_id;
    }

    public void setMs_id(String ms_id) {
        this.ms_id = ms_id;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }
    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }
    public String getRepCode() {
        return repCode;
    }
    public String getRepMsg() {
        return repMsg;
    }
}
