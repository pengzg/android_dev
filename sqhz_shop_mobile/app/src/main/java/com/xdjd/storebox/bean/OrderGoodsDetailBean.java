package com.xdjd.storebox.bean;

import com.xdjd.utils.GoodsNumDesc;
import com.xdjd.utils.StringUtils;

import java.util.List;

/**
 * 订单商品详情bean
 * Created by lijipei on 2016/12/5.
 */

public class OrderGoodsDetailBean{
    private String od_goods_name;//商品名称
    private String od_goods_img;//图片
    private String od_goods_num_min;//商品数量小单位
    private String od_goods_unitname_min;//小单位名称
    private String od_goods_num_max;//商品数量大单位
    private String od_goods_unitname_max;// 大单位名称
    private String od_goods_num;//商品数量合计（小单位）
    private String od_unit;//单位基准
    private String od_unit_num;//单位换算关系
    private String od_price_min;//购买单价（小单位前台）
    private String od_total_amount;//小计
    private String od_delivery_num;//发货数量合计(小单位)
    private String od_delivery_amount;//发货金额
    private String od_delivery_num_min;//发货数量小单位
    private String od_delivery_num_max;//发货数量大单位
    private String od_goodstype;//商品类型
    private String get_goods_num;//要货数量
    private String delivery_goods_num;//发货数量
    private String 	od_price_strategyid;//价格方案id
    private String 	od_goods_price_id;//价格id

    //-----------新参数------------
    private String bpa_path;//	商品图片
    private String gg_title;//	商品名称
    private String gps_price_min;//	商品价格（小单位）
    private String goods_amount;//	商品小计
    private String goods_num;//	商品数量
    private String gg_id;//	商品id
    private String ggp_id;//	商品价格id

    public String getGet_goods_num() {
        GoodsNumDesc desc = new GoodsNumDesc();
        get_goods_num = desc.getGoodsNumDesc(od_goods_num_max,od_goods_unitname_max,od_goods_num_min,
                od_goods_unitname_min,get_goods_num);
        return get_goods_num;
    }

    public void setGet_goods_num(String get_goods_num) {
        this.get_goods_num = get_goods_num;
    }

    public String getDelivery_goods_num() {
        GoodsNumDesc descD = new GoodsNumDesc();
        delivery_goods_num = descD.getGoodsNumDesc(od_delivery_num_max,od_goods_unitname_max,
                od_delivery_num_min,od_goods_unitname_min,delivery_goods_num);
        return delivery_goods_num;
    }


    public String getBpa_path() {
        return bpa_path;
    }

    public void setBpa_path(String bpa_path) {
        this.bpa_path = bpa_path;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getGps_price_min() {
        return gps_price_min;
    }

    public void setGps_price_min(String gps_price_min) {
        this.gps_price_min = gps_price_min;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
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

    public void setDelivery_goods_num(String delivery_goods_num) {
        this.delivery_goods_num = delivery_goods_num;
    }

    public String getOd_goods_name() {
        return od_goods_name;
    }

    public void setOd_goods_name(String od_goods_name) {
        this.od_goods_name = od_goods_name;
    }

    public String getOd_goods_img() {
        return od_goods_img;
    }

    public void setOd_goods_img(String od_goods_img) {
        this.od_goods_img = od_goods_img;
    }

    public String getOd_goods_num_min() {
        return od_goods_num_min;
    }

    public void setOd_goods_num_min(String od_goods_num_min) {
        this.od_goods_num_min = od_goods_num_min;
    }

    public String getOd_goods_unitname_min() {
        return od_goods_unitname_min;
    }

    public void setOd_goods_unitname_min(String od_goods_unitname_min) {
        this.od_goods_unitname_min = od_goods_unitname_min;
    }

    public String getOd_goods_num_max() {
        return od_goods_num_max;
    }

    public void setOd_goods_num_max(String od_goods_num_max) {
        this.od_goods_num_max = od_goods_num_max;
    }

    public String getOd_goods_unitname_max() {
        return od_goods_unitname_max;
    }

    public void setOd_goods_unitname_max(String od_goods_unitname_max) {
        this.od_goods_unitname_max = od_goods_unitname_max;
    }

    public String getOd_goods_num() {
        return od_goods_num;
    }

    public void setOd_goods_num(String od_goods_num) {
        this.od_goods_num = od_goods_num;
    }

    public String getOd_unit() {
        return od_unit;
    }

    public void setOd_unit(String od_unit) {
        this.od_unit = od_unit;
    }

    public String getOd_unit_num() {
        return od_unit_num;
    }

    public void setOd_unit_num(String od_unit_num) {
        this.od_unit_num = od_unit_num;
    }

    public String getOd_price_min() {
        return od_price_min;
    }

    public void setOd_price_min(String od_price_min) {
        this.od_price_min = od_price_min;
    }

    public String getOd_total_amount() {
        return od_total_amount;
    }

    public void setOd_total_amount(String od_total_amount) {
        this.od_total_amount = od_total_amount;
    }

    public String getOd_delivery_num() {
        return od_delivery_num;
    }

    public void setOd_delivery_num(String od_delivery_num) {
        this.od_delivery_num = od_delivery_num;
    }

    public String getOd_delivery_amount() {
        return od_delivery_amount;
    }

    public void setOd_delivery_amount(String od_delivery_amount) {
        this.od_delivery_amount = od_delivery_amount;
    }

    public String getOd_delivery_num_min() {
        return od_delivery_num_min;
    }

    public void setOd_delivery_num_min(String od_delivery_num_min) {
        this.od_delivery_num_min = od_delivery_num_min;
    }

    public String getOd_delivery_num_max() {
        return od_delivery_num_max;
    }

    public void setOd_delivery_num_max(String od_delivery_num_max) {
        this.od_delivery_num_max = od_delivery_num_max;
    }

    public String getOd_goodstype() {
        return od_goodstype;
    }

    public void setOd_goodstype(String od_goodstype) {
        this.od_goodstype = od_goodstype;
    }

    public String getOd_price_strategyid() {
        return od_price_strategyid;
    }

    public void setOd_price_strategyid(String od_price_strategyid) {
        this.od_price_strategyid = od_price_strategyid;
    }

    public String getOd_goods_price_id() {
        return od_goods_price_id;
    }

    public void setOd_goods_price_id(String od_goods_price_id) {
        this.od_goods_price_id = od_goods_price_id;
    }
}
