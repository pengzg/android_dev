package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2016/11/29.
 */

public class CartBean extends BaseBean{

    private String cartNum = "0";//	购物车商品总数量
    private String totalAmount = "0.00";//	总价格
    private String goodsCartNum = "0";//	本商品在购物车中的数量
    private String freeAmount = "0";//	起订金额	0 不限制

    private int isAll = 1;

//    cartNum	购物车中商品总数量
//    totalAmount	购物车中所有商品总价格
//    freeAmount	起订金额	0 不限制
//    cartData	购物车数据

    private String goodsTotalAmount;//	本商品的金额小计

    private CartListBean cartData;

    private String c_goods_num;//	商品总数量（小单位）
    private String c_goods_amount;//	小计


    public String getC_goods_amount() {
        return c_goods_amount;
    }

    public void setC_goods_amount(String c_goods_amount) {
        this.c_goods_amount = c_goods_amount;
    }

    public String getC_goods_num() {
        return c_goods_num;
    }

    public void setC_goods_num(String c_goods_num) {
        this.c_goods_num = c_goods_num;
    }

    public CartListBean getCartData() {
        return cartData;
    }

    public void setCartData(CartListBean cartData) {
        this.cartData = cartData;
    }

    public int getIsAll() {
        return isAll;
    }

    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

    public String getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getGoodsTotalAmount() {
        return goodsTotalAmount;
    }

    public void setGoodsTotalAmount(String goodsTotalAmount) {
        this.goodsTotalAmount = goodsTotalAmount;
    }

    public String getGoodsCartNum() {
        return goodsCartNum;
    }

    public void setGoodsCartNum(String goodsCartNum) {
        this.goodsCartNum = goodsCartNum;
    }


    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
