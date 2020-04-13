package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 商品分类bean
 * Created by lijipei on 2016/11/24.
 */

public class GoodClassifyBean {
    private String repCode;
    private String repMsg;
    private List<GoodClassifyListBean> listData;

    public List<GoodClassifyListBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodClassifyListBean> listData) {
        this.listData = listData;
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

}
