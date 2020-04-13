package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 购物车商品列表bean
 * Created by lijipei on 2016/12/1.
 */

public class CartListBean {
    private String goodsNum;//	购物车商品总数量
    private String amount;//	总价格
    private String freeAmount;//	起订金额
    private String shopName;//	店铺名字
    private String delivery;//	配送方
    private String shopTel;//电话
    private String  modulecode;//配送分类类型
    private int isAll = 1;

    //private List<CartGoodsListBean> dataList;//	商品数据

    private List<CartActionBean> activityList;//	活动列表


    public List<CartActionBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<CartActionBean> activityList) {
        this.activityList = activityList;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getModulecode() {
        return modulecode;
    }

    public void setModulecode(String modulecode) {
        this.modulecode = modulecode;
    }

    public int getIsAll() {
        return isAll;
    }

    public void setIsAll(int isAll) {
        this.isAll = isAll;
    }

//    public List<CartGoodsListBean> getDataList() {
//        return dataList;
//    }
//
//    public void setDataList(List<CartGoodsListBean> dataList) {
//        this.dataList = dataList;
//    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(String freeAmount) {
        this.freeAmount = freeAmount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

}
