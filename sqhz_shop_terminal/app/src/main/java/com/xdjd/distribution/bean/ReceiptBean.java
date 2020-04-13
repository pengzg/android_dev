package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/8
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ReceiptBean extends BaseBean {

//    private String brandId;//	品牌id	Y
    private int type;//	类型	Y	1 预收款 2 收欠款
    private String cashAmount;//	现金金额	Y
    private String cardAmount;//	刷卡金额	Y
    private String disCountAmount;//	优惠金额	Y

    //自己添加的字段
    private String brandName;//品牌名称

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(String cardAmount) {
        this.cardAmount = cardAmount;
    }

    public String getDisCountAmount() {
        return disCountAmount;
    }

    public void setDisCountAmount(String disCountAmount) {
        this.disCountAmount = disCountAmount;
    }
}
