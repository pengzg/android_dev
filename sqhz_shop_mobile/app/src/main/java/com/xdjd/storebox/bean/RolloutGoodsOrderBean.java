package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsOrderBean extends BaseBean implements Serializable{

    private List<RolloutGoodsOrderBean> listData;//	列表

    private String customerId;
    private String orderCode;//	订单编号
    private String totalAmount;//	订单总金额
    private String addTime;//	下单时间
    private String order_id;//	订单id
    private String orderStatus;//	订单状态   销售单时:6-已完成 7-已撤销

    private String orderStatus_nameref;//	订单状态名称
    private String shopName;//	店铺名称	铺货单和销售单

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderStatus_nameref() {
        return orderStatus_nameref;
    }

    public void setOrderStatus_nameref(String orderStatus_nameref) {
        this.orderStatus_nameref = orderStatus_nameref;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<RolloutGoodsOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<RolloutGoodsOrderBean> listData) {
        this.listData = listData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}
