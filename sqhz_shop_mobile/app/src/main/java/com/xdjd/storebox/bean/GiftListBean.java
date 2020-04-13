package com.xdjd.storebox.bean;

/**
 * 购物车赠品列表
 * Created by lijipei on 2017/1/17.
 */

public class GiftListBean {
    private String gp_id;//	Gp_id
    private String goodsId;//	商品id
    private String goodsNum;//	商品数量
    private String goodsImage;//	商品图片
    private String goodsName;//	商品名称
    private String activityDesc;//	赠品描述

    public String getGp_id() {
        return gp_id;
    }

    public void setGp_id(String gp_id) {
        this.gp_id = gp_id;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
