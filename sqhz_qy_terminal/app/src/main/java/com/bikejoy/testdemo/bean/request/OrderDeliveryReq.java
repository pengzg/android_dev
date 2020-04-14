package com.bikejoy.testdemo.bean.request;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2019/4/11
 *     desc   : 发货出库请求
 *     version: 1.0
 * </pre>
 */

public class OrderDeliveryReq {

    private String deliveryStoreId;//仓库id
    private String deliveryTotalNum;//发货数量
    private String deliverymanId;//业务员mbw id
    private String deliveryAmount;//发货总金额
    private String  productAmount;//商品总金额
    private String orderMainId;//订单id
    private String   orderDetailListStr;
	/*（待出库列表返回的字段  json串化 加一个outNum 发货数量）
        od_delivery_num
                od_id
        od_mainid
                od_product_code
        od_product_id
                od_product_name
        od_product_num
                od_product_price
        od_product_skuid
                od_sale_price
        pss_stocknum
        outNum 出库数量*/
    private String  orderCode;
    private String memberId;
    private String consigneename;
    private String consigneemobile;
    private String consigneeaddress;
    private String orderRemarks;

    public String getDeliveryStoreId() {
        return deliveryStoreId;
    }

    public void setDeliveryStoreId(String deliveryStoreId) {
        this.deliveryStoreId = deliveryStoreId;
    }

    public String getDeliveryTotalNum() {
        return deliveryTotalNum;
    }

    public void setDeliveryTotalNum(String deliveryTotalNum) {
        this.deliveryTotalNum = deliveryTotalNum;
    }

    public String getDeliverymanId() {
        return deliverymanId;
    }

    public void setDeliverymanId(String deliverymanId) {
        this.deliverymanId = deliverymanId;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getOrderMainId() {
        return orderMainId;
    }

    public void setOrderMainId(String orderMainId) {
        this.orderMainId = orderMainId;
    }

    public String getOrderDetailListStr() {
        return orderDetailListStr;
    }

    public void setOrderDetailListStr(String orderDetailListStr) {
        this.orderDetailListStr = orderDetailListStr;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getConsigneename() {
        return consigneename;
    }

    public void setConsigneename(String consigneename) {
        this.consigneename = consigneename;
    }

    public String getConsigneemobile() {
        return consigneemobile;
    }

    public void setConsigneemobile(String consigneemobile) {
        this.consigneemobile = consigneemobile;
    }

    public String getConsigneeaddress() {
        return consigneeaddress;
    }

    public void setConsigneeaddress(String consigneeaddress) {
        this.consigneeaddress = consigneeaddress;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }
}
