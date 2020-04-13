package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class OrderPictureBean {
    private String goodsName;//产品名称
    private String goodsImg;//产品图片
    private String goodsDiscountAmount;//发货金额
    private List<OrderPictureBean> pictureBeanList;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsDiscountAmount() {
        return goodsDiscountAmount;
    }

    public void setGoodsDiscountAmount(String goodsDiscountAmount) {
        this.goodsDiscountAmount = goodsDiscountAmount;
    }

    public List<OrderPictureBean> getPictureBeanList() {
        return pictureBeanList;
    }

    public void setPictureBeanList(List<OrderPictureBean> pictureBeanList) {
        this.pictureBeanList = pictureBeanList;
    }
}
