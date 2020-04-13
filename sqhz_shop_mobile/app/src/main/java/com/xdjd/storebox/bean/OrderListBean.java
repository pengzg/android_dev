package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class OrderListBean extends BaseBean {
    private String deliveryAmount;//发货金额
    private String deliveryNum;//数量
    private String distributionModeDesc;//是否默认选中
    private String orderCode;//订单编号
    private String orderId;//订单id
    private String orderStatusName;//订单状态
    private String settlementAmount;//结算金额
    private String totalAmount;//订单总金额
    private String btnValue;//按钮值
    private String goodsNum;
    private String orderDate;//下单时间
    private List<String> goodsCover;//商品图片集合
    List<OrderPictureBean>itemList;
    List<ButtonListBean>buttonList;

    public List<String> getGoodsCover() {
        return goodsCover;
    }

    public void setGoodsCover(List<String> goodsCover) {
        this.goodsCover = goodsCover;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public String getDeliveryNum() {
        return deliveryNum;
    }

    public void setDeliveryNum(String deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    public String getDistributionModeDesc() {
        return distributionModeDesc;
    }

    public void setDistributionModeDesc(String distributionModeDesc) {
        this.distributionModeDesc = distributionModeDesc;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBtnValue() {
        return btnValue;
    }

    public void setBtnValue(String btnValue) {
        this.btnValue = btnValue;
    }

    public List<OrderPictureBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderPictureBean> itemList) {
        this.itemList = itemList;
    }

    public List<ButtonListBean> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonListBean> buttonList) {
        this.buttonList = buttonList;
    }
}
