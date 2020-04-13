package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OperatingBean extends BaseBean {
    private String preAmount = "0.00";//	预收 款
    private String refundAmount = "0.00";//	退款
    private String saleAmount = "0.00";//	销售额
    private String receivableAmount = "0.00";//  应收款
//    private String stockAmount = "0.00";//	库存总金额

    public String getPreAmount() {
        return preAmount;
    }

    public void setPreAmount(String preAmount) {
        this.preAmount = preAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

}
