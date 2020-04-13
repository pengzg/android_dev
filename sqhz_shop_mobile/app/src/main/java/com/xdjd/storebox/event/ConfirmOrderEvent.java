package com.xdjd.storebox.event;

import java.util.List;

/**
 * 点击提交订单后,将提交商品的数据传给采购界面刷新数据
 * Created by lijipei on 2017/3/10.
 */

public class ConfirmOrderEvent {

    private List<String> dataList;

    public ConfirmOrderEvent(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }
}
