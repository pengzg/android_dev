package com.xdjd.distribution.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/9
 *     desc   : 打印单据参数bean
 *     version: 1.0
 * </pre>
 */

public class PrintParamBean<T> implements Serializable {

    private int type;//单据类型
    private String orderType="";//订单类型
    private String orderStatus="";//订单状态
    private ClientBean clientBean;//小店信息
    private UserBean userBean;//业务员信息
    private String title="";//单据标题
    private String time="";//打印时间
    private String orderCode="";//订单编号

    private T t;//参数
    private T t1;//参数1

    private String skAmount = "0.00";//刷卡金额
    private String yhAmount = "0.00";//优惠金额
    private String xjAmount = "0.00";//现金金额
    private String yeAmount = "0.00";//余额
    private String ysAmount = "0.00";//应收款(欠款)
    private String sfAmount = "0.00";//实付金额
    private String totalAmount = "0.00";//总金额
    private boolean isPrintCode;//是否打印商品条码

    private String remarks;//备注
    private Bitmap signImg;//签名图片


    public Bitmap getSignImg() {
        return signImg;
    }

    public void setSignImg(Bitmap signImg) {
        this.signImg = signImg;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public ClientBean getClientBean() {
        return clientBean;
    }

    public void setClientBean(ClientBean clientBean) {
        this.clientBean = clientBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public T getT1() {
        return t1;
    }

    public void setT1(T t1) {
        this.t1 = t1;
    }

    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public String getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(String yhAmount) {
        this.yhAmount = yhAmount;
    }

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getYeAmount() {
        return yeAmount;
    }

    /**
     * 预收款/余额
     * @param yeAmount
     */
    public void setYeAmount(String yeAmount) {
        this.yeAmount = yeAmount;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getSfAmount() {
        return sfAmount;
    }

    public void setSfAmount(String sfAmount) {
        this.sfAmount = sfAmount;
    }

    public boolean isPrintCode() {
        return isPrintCode;
    }

    public void setPrintCode(boolean printCode) {
        isPrintCode = printCode;
    }
}
