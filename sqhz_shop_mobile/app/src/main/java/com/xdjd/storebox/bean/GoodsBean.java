package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2016/11/25.
 */

public class GoodsBean {
    private String repCode;
    private String repMsg;
    private List<GoodsBean> listData;

    private String gg_id;//	商品id
    private String gg_title;//	商品标题
    private String goods_stock;//	库存
    private String supplierid;//	店家id
    private String bpa_path;//	商品图片
    private String gg_simpletitle;//商品简称
    private String goods_modulecode;//	1快销品  2生鲜
    private String gps_min_num;//	起订量
    private String gps_price_min;//	批发价
    private String gps_price_max;//	箱规批发价
    private String gg_unit_max_nameref;//	大单位名
    private String gg_unit_min_nameref;//小单位名称
    private String gps_add_num;//	增量（小单位）
    private String cartnum;//	当前商品在购物车数量
    private String delivery;//配送方式
    private String gg_model;//	规格
    private String ggp_goods_type; //type: 15 为预售商品
    private String gp_alarm_stock; //商品预警数量
    private String ggp_id;//详情id	在跳转商品详情使用
    private String activityId;//商品活动列表
    private String gps_id;//价格方案id


    public String getGps_id() {
        return gps_id;
    }

    public void setGps_id(String gps_id) {
        this.gps_id = gps_id;
    }

    //商品筛选分类集合
    private List<ScreenBean> brandListData;//	品牌数据
    private List<ScreenBean> categoryListData;//	第三级分类数据
    private List<ScreenBean> priceListData;//	价格数据

    public String getGg_id() {
        return gg_id;
    }

    public void setGg_id(String gg_id) {
        this.gg_id = gg_id;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getBpa_path() {
        return bpa_path;
    }

    public void setBpa_path(String bpa_path) {
        this.bpa_path = bpa_path;
    }

    public String getGg_simpletitle() {
        return gg_simpletitle;
    }

    public void setGg_simpletitle(String gg_simpletitle) {
        this.gg_simpletitle = gg_simpletitle;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getGgp_id() {
        return ggp_id;
    }

    public void setGgp_id(String ggp_id) {
        this.ggp_id = ggp_id;
    }

    public String getGp_alarm_stock() {
        return gp_alarm_stock;
    }

    public void setGp_alarm_stock(String gp_alarm_stock) {
        this.gp_alarm_stock = gp_alarm_stock;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public List<ScreenBean> getBrandListData() {
        return brandListData;
    }

    public void setBrandListData(List<ScreenBean> brandListData) {
        this.brandListData = brandListData;
    }

    public List<ScreenBean> getCategoryListData() {
        return categoryListData;
    }

    public void setCategoryListData(List<ScreenBean> categoryListData) {
        this.categoryListData = categoryListData;
    }

    public List<ScreenBean> getPriceListData() {
        return priceListData;
    }

    public void setPriceListData(List<ScreenBean> priceListData) {
        this.priceListData = priceListData;
    }

    public String getRepCode() {
        return repCode;
    }

    public void setRepCode(String repCode) {
        this.repCode = repCode;
    }

    public String getRepMsg() {
        return repMsg;
    }

    public void setRepMsg(String repMsg) {
        this.repMsg = repMsg;
    }

    public List<GoodsBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodsBean> listData) {
        this.listData = listData;
    }


    public String getGoods_modulecode() {
        return goods_modulecode;
    }

    public void setGoods_modulecode(String goods_modulecode) {
        this.goods_modulecode = goods_modulecode;
    }

    public String getGps_min_num() {
        return gps_min_num;
    }

    public void setGps_min_num(String gps_min_num) {
        this.gps_min_num = gps_min_num;
    }

    public String getGps_add_num() {
        return gps_add_num;
    }

    public void setGps_add_num(String gps_add_num) {
        this.gps_add_num = gps_add_num;
    }


    public String getCartnum() {
        return cartnum;
    }

    public void setCartnum(String cartnum) {
        this.cartnum = cartnum;
    }

    public String getGps_price_min() {
        return gps_price_min;
    }

    public void setGps_price_min(String gps_price_min) {
        this.gps_price_min = gps_price_min;
    }

    public String getGps_price_max() {
        return gps_price_max;
    }

    public void setGps_price_max(String gps_price_max) {
        this.gps_price_max = gps_price_max;
    }

    public String getGg_unit_max_nameref() {
        return gg_unit_max_nameref;
    }

    public void setGg_unit_max_nameref(String gg_unit_max_nameref) {
        this.gg_unit_max_nameref = gg_unit_max_nameref;
    }

    public String getGg_unit_min_nameref() {
        return gg_unit_min_nameref;
    }

    public void setGg_unit_min_nameref(String gg_unit_min_nameref) {
        this.gg_unit_min_nameref = gg_unit_min_nameref;
    }

    public String getGgp_goods_type() {
        return ggp_goods_type;
    }

    public void setGgp_goods_type(String ggp_goods_type) {
        this.ggp_goods_type = ggp_goods_type;
    }

    public String getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(String goods_stock) {
        this.goods_stock = goods_stock;
    }
}
