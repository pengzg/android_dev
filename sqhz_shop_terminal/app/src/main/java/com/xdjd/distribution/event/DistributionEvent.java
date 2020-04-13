package com.xdjd.distribution.event;

/**
 * Created by lijipei on 2017/6/7.
 */

public class DistributionEvent {

    private int orderType;
    private int listIndex;

    public DistributionEvent(int orderType,int listIndex) {
        this.orderType = orderType;
        this.listIndex = listIndex;
    }

    public int getListIndex() {
        return listIndex;
    }

    public int getOrderType() {
        return orderType;
    }
}
