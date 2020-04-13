package com.xdjd.storebox.bean;

/**
 * 积分结算bean
 * Created by lijipei on 2017/3/1.
 */

public class IntegralConfirmBean extends BaseBean{

    private String userName;//	用户名
    private String mobile;//	用户手机号
    private String address;//	送货地址
    private String goodsName;//	商品标题
    private String goodsImage;//	商品图片
    private String integrate_price;//	商品积分
    private String goodsNums;//	商品数量
    private String total_price;//	商品总积分
    private String gp_wholesale_price;//市场参考价

    public String getGp_wholesale_price() {
        return gp_wholesale_price;
    }

    public void setGp_wholesale_price(String gp_wholesale_price) {
        this.gp_wholesale_price = gp_wholesale_price;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getIntegrate_price() {
        return integrate_price;
    }

    public void setIntegrate_price(String integrate_price) {
        this.integrate_price = integrate_price;
    }

    public String getGoodsNums() {
        return goodsNums;
    }

    public void setGoodsNums(String goodsNums) {
        this.goodsNums = goodsNums;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
