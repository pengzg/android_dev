package com.xdjd.distribution.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/6
 *     desc   : 配送
 *     version: 1.0
 * </pre>
 */

public class DistributionGoodsBean extends BaseBean implements Serializable{
    private String od_id;//	订单明细id
    private String od_goods_name;//	商品名称
    private String od_createdate;//	生产日期
    private String od_goods_num_min;//	小单位数量
    private String od_goods_num_max;//	大单位数量
    private String od_goods_unitname_min;//	小单位描述
    private String od_goods_unitname_max;//	大单位描述
    private String gg_model;//	商品规格
    private String od_price_min;//	商品小单位价格
    private String od_price_max;//	商品大单位价格
    private String od_real_total;//	商品总价格
    private String od_goods_price_id;//	商品价格主键
    private String od_unit_num;//大小单位换算数量
    private String od_price_strategyid;//	价格方案id

    private String od_goodstype;//	商品类型 1 普通 2 赠品

    private String gg_international_code_max = "";// 大单位
    private String gg_international_code_min = "";//小单位

//    private String od_stock = "";//自定义参数
    private String od_goods_state;//	商品状态	商品状态 1 正 2 临 3残 4过
    private String od_goods_state_nameref;//	商品状态


    public String getOd_goods_state() {
        return od_goods_state;
    }

    public void setOd_goods_state(String od_goods_state) {
        this.od_goods_state = od_goods_state;
    }

    public String getOd_goods_state_nameref() {
        return od_goods_state_nameref;
    }

    public void setOd_goods_state_nameref(String od_goods_state_nameref) {
        this.od_goods_state_nameref = od_goods_state_nameref;
    }

    public String getOd_price_strategyid() {
        return od_price_strategyid;
    }

    public void setOd_price_strategyid(String od_price_strategyid) {
        this.od_price_strategyid = od_price_strategyid;
    }

    public String getOd_goodstype() {
        return od_goodstype;
    }

    public void setOd_goodstype(String od_goodstype) {
        this.od_goodstype = od_goodstype;
    }

    public String getGg_international_code_max() {
        return gg_international_code_max;
    }

    public void setGg_international_code_max(String gg_international_code_max) {
        this.gg_international_code_max = gg_international_code_max;
    }

    public String getGg_international_code_min() {
        return gg_international_code_min;
    }

    public void setGg_international_code_min(String gg_international_code_min) {
        this.gg_international_code_min = gg_international_code_min;
    }

    public String getOd_unit_num() {
        return od_unit_num;
    }

    public void setOd_unit_num(String od_unit_num) {
        this.od_unit_num = od_unit_num;
    }

    public String getOd_goods_price_id() {
        return od_goods_price_id;
    }

    public void setOd_goods_price_id(String od_goods_price_id) {
        this.od_goods_price_id = od_goods_price_id;
    }

    public String getOd_id() {
        return od_id;
    }

    public void setOd_id(String od_id) {
        this.od_id = od_id;
    }

    public String getOd_goods_name() {
        return od_goods_name;
    }

    public void setOd_goods_name(String od_goods_name) {
        this.od_goods_name = od_goods_name;
    }

    public String getOd_createdate() {
        return od_createdate;
    }

    public void setOd_createdate(String od_createdate) {
        this.od_createdate = od_createdate;
    }

    public String getOd_goods_num_min() {
        return od_goods_num_min;
    }

    public void setOd_goods_num_min(String od_goods_num_min) {
        this.od_goods_num_min = od_goods_num_min;
    }

    public String getOd_goods_num_max() {
        return od_goods_num_max;
    }

    public void setOd_goods_num_max(String od_goods_num_max) {
        this.od_goods_num_max = od_goods_num_max;
    }

    public String getOd_goods_unitname_min() {
        return od_goods_unitname_min;
    }

    public void setOd_goods_unitname_min(String od_goods_unitname_min) {
        this.od_goods_unitname_min = od_goods_unitname_min;
    }

    public String getOd_goods_unitname_max() {
        return od_goods_unitname_max;
    }

    public void setOd_goods_unitname_max(String od_goods_unitname_max) {
        this.od_goods_unitname_max = od_goods_unitname_max;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getOd_price_min() {
        return od_price_min;
    }

    public void setOd_price_min(String od_price_min) {
        this.od_price_min = od_price_min;
    }

    public String getOd_price_max() {
        return od_price_max;
    }

    public void setOd_price_max(String od_price_max) {
        this.od_price_max = od_price_max;
    }

    public String getOd_real_total() {
        return od_real_total;
    }

    public void setOd_real_total(String od_real_total) {
        this.od_real_total = od_real_total;
    }
}
