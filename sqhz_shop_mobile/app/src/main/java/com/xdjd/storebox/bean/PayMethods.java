package com.xdjd.storebox.bean;

/**
 * 支付方式bean
 */
public class PayMethods {

    private String payId;
    private String payName;
    private String isDefault;
    private String payImg;
    private String payTitle;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getPayImg() {
        return payImg;
    }

    public void setPayImg(String payImg) {
        this.payImg = payImg;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }
}
