package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/3/1.
 */

public class IntegralGoodsBean extends BaseBean {
    private List<IntegralGoodsBean>listData;
    private int wig_id;
    private String wig_integrate_price;//积分数
    private  int wig_stock;//积分库存
    private String goods_title;//积分商品标题
    private String gp_wholesale_price;//市场价
    private String path;//图片

    public List<IntegralGoodsBean> getListData() {
        return listData;
    }

    public void setListData(List<IntegralGoodsBean> listData) {
        this.listData = listData;
    }

    public int getWig_id() {
        return wig_id;
    }

    public void setWig_id(int wig_id) {
        this.wig_id = wig_id;
    }

    public String getWig_integrate_price() {
        return wig_integrate_price;
    }

    public void setWig_integrate_price(String wig_integrate_price) {
        this.wig_integrate_price = wig_integrate_price;
    }

    public int  getWig_stock() {
        return wig_stock;
    }

    public void setWig_stock(int wig_stock) {
        this.wig_stock = wig_stock;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGp_wholesale_price() {
        return gp_wholesale_price;
    }

    public void setGp_wholesale_price(String gp_wholesale_price) {
        this.gp_wholesale_price = gp_wholesale_price;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
