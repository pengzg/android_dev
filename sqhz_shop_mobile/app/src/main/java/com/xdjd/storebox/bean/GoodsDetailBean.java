package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 商品详情bean
 * Created by lijipei on 2016/11/30.
 */

public class GoodsDetailBean extends BaseBean{
    private String gg_id;//商品id
    private String gg_title;//商品标题
    private String bpa_path;//图片地址
    private List<String>bpa_path_list;//图片集合
    private String gg_model;//规格
    private String ggp_id;//商品价格id
    private String ggp_goods_type;//商品类型
    private String ggp_unit_num;//换算关系
    private String gps_price_max;//大单位价格
    private String gps_price_min;//小单位价格
    //private String ggs_stock;//库存小单位
    private String goods_stock;//库存小单位
    private String ggp_stock_desc;//库存描述
    private String gg_unit_max_nameref;//大单位名称
    private String gg_unit_min_nameref;//小单位名称
    private String gps_id;//价格方案id
    private String gps_add_num;//最新增量（小单位）
    private String gps_min_num;//起订量 小单位
    private String goods_content = "";//商品详情
    private String goods_intro;//商品简介

    private String activityName;//活动名称
    private String activityId;//活动id
    private String activityDesc;//活动描述
    private String activityType;//活动类型


    private String isFavorite;//	是否收藏 	1收藏 2取消收藏

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public List<String> getBpa_path_list() {
        return bpa_path_list;
    }

    public void setBpa_path_list(List<String> bpa_path_list) {
        this.bpa_path_list = bpa_path_list;
    }

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

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getGgp_id() {
        return ggp_id;
    }

    public void setGgp_id(String ggp_id) {
        this.ggp_id = ggp_id;
    }

    public String getGgp_goods_type() {
        return ggp_goods_type;
    }

    public void setGgp_goods_type(String ggp_goods_type) {
        this.ggp_goods_type = ggp_goods_type;
    }

    public String getGgp_unit_num() {
        return ggp_unit_num;
    }

    public void setGgp_unit_num(String ggp_unit_num) {
        this.ggp_unit_num = ggp_unit_num;
    }

    public String getGps_price_max() {
        return gps_price_max;
    }

    public void setGps_price_max(String gps_price_max) {
        this.gps_price_max = gps_price_max;
    }

    public String getGps_price_min() {
        return gps_price_min;
    }

    public void setGps_price_min(String gps_price_min) {
        this.gps_price_min = gps_price_min;
    }

    public String getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(String goods_stock) {
        this.goods_stock = goods_stock;
    }

    public String getGgp_stock_desc() {
        return ggp_stock_desc;
    }

    public void setGgp_stock_desc(String ggp_stock_desc) {
        this.ggp_stock_desc = ggp_stock_desc;
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

    public String getGps_id() {
        return gps_id;
    }

    public void setGps_id(String gps_id) {
        this.gps_id = gps_id;
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

    public String getGoods_content() {
        return goods_content;
    }

    public void setGoods_content(String goods_content) {
        this.goods_content = goods_content;
    }

    public String getGoods_intro() {
        return goods_intro;
    }

    public void setGoods_intro(String goods_intro) {
        this.goods_intro = goods_intro;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
}
