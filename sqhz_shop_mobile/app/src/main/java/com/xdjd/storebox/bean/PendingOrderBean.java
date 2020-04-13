package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PendingOrderBean extends BaseBean {
    private List<PendingOrderBean>listData;
    private String shopName;//店铺名
    private String orderCode;//订单编号
    private String addtime;//下单时间
    private String receiverName;//收货人姓名
    private String mobile;//收货人手机号
    private String address;//收货地址
    private String orderStatus_nameref;//订单状态
    private String goodsNum;//商品总件数
    private String totalAmount;//订单金额
    private String settlementAmount;//结算金额
    private String orderId;//订单id

    public String getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PendingOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<PendingOrderBean> listData) {
        this.listData = listData;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderStatus_nameref() {
        return orderStatus_nameref;
    }

    public void setOrderStatus_nameref(String orderStatus_nameref) {
        this.orderStatus_nameref = orderStatus_nameref;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
