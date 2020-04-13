package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/22
 *     desc   : 订单信息列表bean
 *     version: 1.0
 * </pre>
 */

public class OrderListBean implements Serializable{

    private List<OrderListBean> listOrder;

    /**
     * 订单类型 1 普通 2 处理 3 退货 4 换货 5还货
     */
    private int orderType;
    private String order_type_amount; //总价格
    private List<GoodsBean> listData;//订单数据
    private List<OrderDetailStrBean> listSettlement;//订单结算拼接的参数

    public List<OrderDetailStrBean> getListSettlement() {
        return listSettlement;
    }

    public void setListSettlement(List<OrderDetailStrBean> listSettlement) {
        this.listSettlement = listSettlement;
    }

    public String getOrder_type_amount() {
        return order_type_amount;
    }

    public void setOrder_type_amount(String order_type_amount) {
        this.order_type_amount = order_type_amount;
    }

    public List<OrderListBean> getListOrder() {
        return listOrder;
    }

    public void setListOrder(List<OrderListBean> listOrder) {
        this.listOrder = listOrder;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public List<GoodsBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodsBean> listData) {
        this.listData = listData;
    }
}
