package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 活动轮播图,列表
 * Created by lijipei on 2016/12/9.
 */

public class ActionGoodsBean extends BaseBean{
    private List<ActionGoodsBean> dataList;//		活动list集合

    private String goodsId;//	商品id
    private String gbTitle;//	商品标题
    private String goodsStock;//	库存
    private String gbCover;//	商品图片
    private String delivery;//	配送方式
    private String standard;//	规格描述
    private String gp_minnum;//	起订量
    private String gp_wholesale_price;//	批发价
    private String gp_wholesale_relation_price;//	箱规批发价
    private String gp_addnum;//	增量
    private String cartnum;//	购物车数量
    private String gpId;//	在跳转商品详情使用

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGbTitle() {
        return gbTitle;
    }

    public void setGbTitle(String gbTitle) {
        this.gbTitle = gbTitle;
    }

    public String getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(String goodsStock) {
        this.goodsStock = goodsStock;
    }

    public String getGbCover() {
        return gbCover;
    }

    public void setGbCover(String gbCover) {
        this.gbCover = gbCover;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getGp_minnum() {
        return gp_minnum;
    }

    public void setGp_minnum(String gp_minnum) {
        this.gp_minnum = gp_minnum;
    }

    public String getGp_wholesale_price() {
        return gp_wholesale_price;
    }

    public void setGp_wholesale_price(String gp_wholesale_price) {
        this.gp_wholesale_price = gp_wholesale_price;
    }

    public String getGp_wholesale_relation_price() {
        return gp_wholesale_relation_price;
    }

    public void setGp_wholesale_relation_price(String gp_wholesale_relation_price) {
        this.gp_wholesale_relation_price = gp_wholesale_relation_price;
    }

    public String getGp_addnum() {
        return gp_addnum;
    }

    public void setGp_addnum(String gp_addnum) {
        this.gp_addnum = gp_addnum;
    }

    public String getCartnum() {
        return cartnum;
    }

    public void setCartnum(String cartnum) {
        this.cartnum = cartnum;
    }

    public String getGpId() {
        return gpId;
    }

    public void setGpId(String gpId) {
        this.gpId = gpId;
    }

    public List<ActionGoodsBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ActionGoodsBean> dataList) {
        this.dataList = dataList;
    }


}
