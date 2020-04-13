package com.xdjd.distribution.event;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/8
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderDeclareEvent {

    private int orderType;

    public OrderDeclareEvent(int orderType) {
        this.orderType = orderType;
    }

    public int getOrderType() {
        return orderType;
    }
}
