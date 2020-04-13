package com.xdjd.storebox.bean;

/**
 * Created by lijipei on 2016/12/6.
 */

public class CreateOrderBean extends BaseBean{

    private String orderId;//	订单ID
    private String orderCode;//	订单编号
    private String payAmount;//	需支付金额
    private String businessName;//	商家名称
    private String businessCode;//	商家编号
    private String certificate;//	证书
    private String payMethod;//	支付方式	1-微信 2-支付宝 3-货到付款
    private String appKey;//	密钥	支付方式为微信时时微信密钥，支付方式为支付宝时，为支付宝私钥
            //支付宝信息
    private String partner;//	合作者身份id
    private String seller;//	邮箱
    //微信统一下单接口返回信息（payMethod为1时有值）
    private String sign;//	签名
    private String prepayId;//	预支付交易会话标识
    private String appid;//	微信分配的公众账号ID
    private String partnerid;//	微信支付分配的商户号
    private String noncestr;//	随机字符串，不长于32位
    private String timestamp;//	时间戳
    private String cartNum;//	购物车数量
    private String cartTotalAmount;//	购物车金额

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public String getCartTotalAmount() {
        return cartTotalAmount;
    }

    public void setCartTotalAmount(String cartTotalAmount) {
        this.cartTotalAmount = cartTotalAmount;
    }
}
