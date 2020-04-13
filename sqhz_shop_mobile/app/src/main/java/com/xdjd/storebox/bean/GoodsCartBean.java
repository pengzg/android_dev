package com.xdjd.storebox.bean;

import java.math.BigDecimal;

/**
 * Created by freestyle_hong on 2017/8/9.
 */

public class GoodsCartBean extends BaseBean {
    private String c_goods_num_max;//大单位数量
    private String c_unitid_max_nameref;//大单位名称
    private String c_goods_num_min;//小单位数量
    private String c_unitid_min_nameref;//小单位名称
    private String 	c_goods_num;//商品总数量(小单位)
    private String c_goods_amount;//小计
    private String gps_add_num;//增量小单位
    private String gps_min_num;//起订量小单位

    private String cartNum;//	购物车总数量
    private String totalAmount;//	购物车总金额
    private String isFavorite = "";//	是否收藏 	1收藏 2取消收藏

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public String getC_goods_num_max() {
        return c_goods_num_max;
    }

    public void setC_goods_num_max(String c_goods_num_max) {
        this.c_goods_num_max = c_goods_num_max;
    }

    public String getC_unitid_max_nameref() {
        return c_unitid_max_nameref;
    }

    public void setC_unitid_max_nameref(String c_unitid_max_nameref) {
        this.c_unitid_max_nameref = c_unitid_max_nameref;
    }

    public String getC_goods_num_min() {
        return c_goods_num_min;
    }

    public void setC_goods_num_min(String c_goods_num_min) {
        this.c_goods_num_min = c_goods_num_min;
    }

    public String getC_unitid_min_nameref() {
        return c_unitid_min_nameref;
    }

    public void setC_unitid_min_nameref(String c_unitid_min_nameref) {
        this.c_unitid_min_nameref = c_unitid_min_nameref;
    }

    public String getC_goods_num() {
        return c_goods_num;
    }

    public void setC_goods_num(String c_goods_num) {
        this.c_goods_num = c_goods_num;
    }

    public String getC_goods_amount() {
        return c_goods_amount;
    }

    public void setC_goods_amount(String c_goods_amount) {
        this.c_goods_amount = c_goods_amount;
    }

    public String getGps_add_num() {
        return gps_add_num;
    }

    public void setGps_add_num(String gps_add_num) {
        this.gps_add_num = gps_add_num;
    }

    public String getGps_min_num() {
        return gps_min_num;
    }

    public void setGps_min_num(String gps_min_num) {
        this.gps_min_num = gps_min_num;
    }
}
