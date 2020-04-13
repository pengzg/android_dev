package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/3/1.
 */

public class IntegralGoodsDetailBean extends BaseBean {
    private String wig_id;
    private String integrate_price;//积分数
    private String goods_content;//商品描述
    private String goods_tittle;//积分商品标题
    private String goods_price;//市场价
    private List<String> imageList;//商品图片集合
    private int wig_stock;//积分商品库存

    public int getWig_stock() {
        return wig_stock;
    }

    public void setWig_stock(int wig_stock) {
        this.wig_stock = wig_stock;
    }

    public String getWig_id() {
        return wig_id;
    }

    public void setWig_id(String wig_id) {
        this.wig_id = wig_id;
    }

    public String getIntegrate_price() {
        return integrate_price;
    }

    public void setIntegrate_price(String integrate_price) {
        this.integrate_price = integrate_price;
    }

    public String getGoods_content() {
        return goods_content;
    }

    public void setGoods_content(String goods_content) {
        this.goods_content = goods_content;
    }

    public String getGoods_tittle() {
        return goods_tittle;
    }

    public void setGoods_tittle(String goods_tittle) {
        this.goods_tittle = goods_tittle;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public List<String> getImageList() {
        return imageList;
    }



    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
