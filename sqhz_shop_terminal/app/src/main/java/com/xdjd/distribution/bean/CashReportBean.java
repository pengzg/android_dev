package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CashReportBean extends BaseBean {

    private String paidAmount = "0.00";//	上缴金额
    private String respondReceivableAmount = "0.00";//	应收金额
    private String cardAmount = "0.00";//	刷卡金额
    private String cardFee = "0.00";//	刷卡费用
    private String saleAmount = "0.00";//	销售金额
    private String goodsAmount = "0.00";//订货金额
    private String useAmount = "0.00";//	使用金额
    private String discountAmount = "0.00";//	优惠金额
    private String receivableAmount = "0.00";//	收款金额
    private String payAmount = "0.00";//	付款金额
    private String refundGiroAmount = "0.00";//	退款转账预收

    private List<CashReportBean> listData;

    private String customerid;//	客户id
    private String custmerName;//	客户名称
    private String customerCode;//	客户编号
    private String customerType;//	客户类别
    private String customerTypeName;//	客户类别名称
//    paidAmount	上交款
//    cardAmount	刷卡金额
//    respondReceivableAmount	应收金额
//    useAmount	使用金额
//    discountAmount	优惠金额
//    cardFee	刷卡费用
    private String totalAmount;//	总金额


    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRespondReceivableAmount() {
        return respondReceivableAmount;
    }

    public void setRespondReceivableAmount(String respondReceivableAmount) {
        this.respondReceivableAmount = respondReceivableAmount;
    }

    public String getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getCardFee() {
        return cardFee;
    }

    public void setCardFee(String cardFee) {
        this.cardFee = cardFee;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(String useAmount) {
        this.useAmount = useAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getRefundGiroAmount() {
        return refundGiroAmount;
    }

    public void setRefundGiroAmount(String refundGiroAmount) {
        this.refundGiroAmount = refundGiroAmount;
    }

    public List<CashReportBean> getListData() {
        return listData;
    }

    public void setListData(List<CashReportBean> listData) {
        this.listData = listData;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getCustmerName() {
        return custmerName;
    }

    public void setCustmerName(String custmerName) {
        this.custmerName = custmerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
