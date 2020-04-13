package com.xdjd.storebox.bean;

import java.io.Serializable;

/**
 * Created by lijipei on 2017/1/17.
 */

public class CartGoodsListBean implements Serializable{

    private String c_goods_amount;//	小计
    private String c_id;//	购物车id
    private String c_goods_num;//	商品数量
    private String c_goods_num_max;//	大单位数量
    private String c_goods_num_min;//	小单位数量
    private String gg_id;//	商品id
    private String ggp_id;//	商品价格id
    //private String ggs_stock = "0";//	库存
    private String goods_stock = "0";
    private String ggp_unit_num = "1";//	换算关系
    private String ggp_goods_type;//	商品类型	1普通2预售
    private String gps_add_num = "1";//	增量（小单位）
    private String gps_min_num = "1";//	起订量（小单位）
    private String gps_price_max;//	大单价
    private String gps_price_min;//	小单价
    private String bpa_path;//	商品图片
    private String gg_unit_min_nameref = "";//	小单位名称
    private String gg_unit_max_nameref = "";//	大单位名称
    private String gg_title = ""; //	商品名称
    private String gg_model;//规格
    private String gps_id; //商品价格方案id

    private int isChild = 0; //是否选中,1是选中, 其他未选中

    public String getGps_id() {
        return gps_id;
    }

    public void setGps_id(String gps_id) {
        this.gps_id = gps_id;
    }

    public int getIsChild() {
        return isChild;
    }

    public void setIsChild(int isChild) {
        this.isChild = isChild;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getC_goods_amount() {
        return c_goods_amount;
    }

    public void setC_goods_amount(String c_goods_amount) {
        this.c_goods_amount = c_goods_amount;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_goods_num() {
        return c_goods_num;
    }

    public void setC_goods_num(String c_goods_num) {
        this.c_goods_num = c_goods_num;
    }

    public String getC_goods_num_max() {
        return c_goods_num_max;
    }

    public void setC_goods_num_max(String c_goods_num_max) {
        this.c_goods_num_max = c_goods_num_max;
    }

    public String getC_goods_num_min() {
        return c_goods_num_min;
    }

    public void setC_goods_num_min(String c_goods_num_min) {
        this.c_goods_num_min = c_goods_num_min;
    }

    public String getGg_id() {
        return gg_id;
    }

    public void setGg_id(String gg_id) {
        this.gg_id = gg_id;
    }

    public String getGgp_id() {
        return ggp_id;
    }

    public void setGgp_id(String ggp_id) {
        this.ggp_id = ggp_id;
    }

    public String getGoods_stock() {
        return goods_stock;
    }

    public void setGoods_stock(String goods_stock) {
        this.goods_stock = goods_stock;
    }

    public String getGgp_unit_num() {
        return ggp_unit_num;
    }

    public void setGgp_unit_num(String ggp_unit_num) {
        this.ggp_unit_num = ggp_unit_num;
    }

    public String getGgp_goods_type() {
        return ggp_goods_type;
    }

    public void setGgp_goods_type(String ggp_goods_type) {
        this.ggp_goods_type = ggp_goods_type;
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

    public String getBpa_path() {
        return bpa_path;
    }

    public void setBpa_path(String bpa_path) {
        this.bpa_path = bpa_path;
    }

    public String getGg_unit_min_nameref() {
        return gg_unit_min_nameref;
    }

    public void setGg_unit_min_nameref(String gg_unit_min_nameref) {
        this.gg_unit_min_nameref = gg_unit_min_nameref;
    }

    public String getGg_unit_max_nameref() {
        return gg_unit_max_nameref;
    }

    public void setGg_unit_max_nameref(String gg_unit_max_nameref) {
        this.gg_unit_max_nameref = gg_unit_max_nameref;
    }
}
