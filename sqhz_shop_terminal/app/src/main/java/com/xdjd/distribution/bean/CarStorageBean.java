package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CarStorageBean extends BaseBean {

    private List<CarStorageBean> listData;//	商品列表

    private String ggs_stock;//	商品可用 库存
    private String ggp_price;//	商品价格
    private String ggp_unit_type;
    private String ggp_unit_num;
    private String gg_title;//	商品名称
    private String gg_model;//	商品规格
    private String gg_unit_max;
    private String gg_unit_min;
    private String instock_num_yes;//	昨日入库
    private String instock_num_now;//	今日入库数
    private String outstock_num_now;//	今日出库数
    private String outstock_num_pre;//	预出库数
    private String adjust_num;//	调整数量
    private String min_price_desc;//	最小价格描述
    private String max_price_desc;//	最大价格描述
    private String goodsAmount;//	商品金额

    private String outstock_num_yes;//	昨日出库
    private String gg_unit_max_nameref;//	大单位名
    private String gg_unit_min_nameref;//	小单位名

    public String getOutstock_num_yes() {
        return outstock_num_yes;
    }

    public void setOutstock_num_yes(String outstock_num_yes) {
        this.outstock_num_yes = outstock_num_yes;
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

    public List<CarStorageBean> getListData() {
        return listData;
    }

    public void setListData(List<CarStorageBean> listData) {
        this.listData = listData;
    }

    public String getGgs_stock() {
        return ggs_stock;
    }

    public void setGgs_stock(String ggs_stock) {
        this.ggs_stock = ggs_stock;
    }

    public String getGgp_price() {
        return ggp_price;
    }

    public void setGgp_price(String ggp_price) {
        this.ggp_price = ggp_price;
    }

    public String getGgp_unit_type() {
        return ggp_unit_type;
    }

    public void setGgp_unit_type(String ggp_unit_type) {
        this.ggp_unit_type = ggp_unit_type;
    }

    public String getGgp_unit_num() {
        return ggp_unit_num;
    }

    public void setGgp_unit_num(String ggp_unit_num) {
        this.ggp_unit_num = ggp_unit_num;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getGg_unit_max() {
        return gg_unit_max;
    }

    public void setGg_unit_max(String gg_unit_max) {
        this.gg_unit_max = gg_unit_max;
    }

    public String getGg_unit_min() {
        return gg_unit_min;
    }

    public void setGg_unit_min(String gg_unit_min) {
        this.gg_unit_min = gg_unit_min;
    }

    public String getInstock_num_yes() {
        return instock_num_yes;
    }

    public void setInstock_num_yes(String instock_num_yes) {
        this.instock_num_yes = instock_num_yes;
    }

    public String getInstock_num_now() {
        return instock_num_now;
    }

    public void setInstock_num_now(String instock_num_now) {
        this.instock_num_now = instock_num_now;
    }

    public String getOutstock_num_now() {
        return outstock_num_now;
    }

    public void setOutstock_num_now(String outstock_num_now) {
        this.outstock_num_now = outstock_num_now;
    }

    public String getOutstock_num_pre() {
        return outstock_num_pre;
    }

    public void setOutstock_num_pre(String outstock_num_pre) {
        this.outstock_num_pre = outstock_num_pre;
    }

    public String getAdjust_num() {
        return adjust_num;
    }

    public void setAdjust_num(String adjust_num) {
        this.adjust_num = adjust_num;
    }

    public String getMin_price_desc() {
        return min_price_desc;
    }

    public void setMin_price_desc(String min_price_desc) {
        this.min_price_desc = min_price_desc;
    }

    public String getMax_price_desc() {
        return max_price_desc;
    }

    public void setMax_price_desc(String max_price_desc) {
        this.max_price_desc = max_price_desc;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }
}
