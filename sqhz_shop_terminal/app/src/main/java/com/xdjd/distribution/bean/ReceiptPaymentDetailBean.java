package com.xdjd.distribution.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/6/11.
 */

public class ReceiptPaymentDetailBean extends BaseBean {

    private List<ReceiptPaymentDetailBean> listData;//	List集合数据

    private String billCode;
    private String orderCode;//	订单编号
    private String typeName;//	单据类型
    private String cardAmount = "0.00";//	刷卡金额
    private String discountAmount = "0.00";//	优惠金额
    private String cashAmount = "0.00";//	现金
    private String billDate;//	单据日期
    private String counterName;//	经办人
    private String gc_stats_nameref;//	审核状态
    private String type;//	单据类型	2101 预收款 2102 订单还货 2103 商品销售 2104 收欠款 2105 销售订货 2106 销售退款
    private String gc_stats;//		0:作废 1：未审核 2：已审核 3：审核不通过 4：已取消
    private String gc_id;

    private String gc_isred;//	是否红冲	Y是  N不是
    private String gc_remarks;//	备注


    public String getGc_isred() {
        return gc_isred;
    }

    public void setGc_isred(String gc_isred) {
        this.gc_isred = gc_isred;
    }

    public String getGc_remarks() {
        return gc_remarks;
    }

    public void setGc_remarks(String gc_remarks) {
        this.gc_remarks = gc_remarks;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGc_stats() {
        return gc_stats;
    }

    public void setGc_stats(String gc_stats) {
        this.gc_stats = gc_stats;
    }

    public String getGc_stats_nameref() {
        return gc_stats_nameref;
    }

    public void setGc_stats_nameref(String gc_stats_nameref) {
        this.gc_stats_nameref = gc_stats_nameref;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public List<ReceiptPaymentDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<ReceiptPaymentDetailBean> listData) {
        this.listData = listData;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }
}
