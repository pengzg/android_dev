package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/8/17.
 */

public class CancelOrderReasonBean extends BaseBean {
    private String 	cancelId;//取消id
    private String cancelMsg;//取消描述
    List<CancelOrderReasonBean> listData;

    public String getCancelId() {
        return cancelId;
    }

    public void setCancelId(String cancelId) {
        this.cancelId = cancelId;
    }

    public String getCancelMsg() {
        return cancelMsg;
    }

    public void setCancelMsg(String cancelMsg) {
        this.cancelMsg = cancelMsg;
    }

    public List<CancelOrderReasonBean> getListData() {
        return listData;
    }

    public void setListData(List<CancelOrderReasonBean> listData) {
        this.listData = listData;
    }
}
