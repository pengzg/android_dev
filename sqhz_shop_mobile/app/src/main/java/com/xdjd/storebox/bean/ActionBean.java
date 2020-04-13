package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 活动商品bean
 * Created by lijipei on 2016/12/12.
 */

public class ActionBean extends BaseBean{

    private String activityCover; //活动封面图片
    /*private List<ActionBean> dataList;//商品数据*/
    private List<ActionBean> listData;//商品数据
    private String gg_id;//商品id
    private String gg_title;//商品标题
    private String bpa_path;//图片地址
    private String gg_model;//规格
    private String ggp_id;//商品价格id
    private String ggp_goods_type;//商品类型 1普通   2预售
    private String ggp_unit_num;//换算关系
    private String gps_price_max;//大单位价格
    private String gps_price_min;//小单位价格
    private String goods_stock;//库存(小单位)-新
    private String ggp_stock_desc;//库存描述
    private String gg_unit_max_nameref;//大单位名称
    private String gg_unit_min_nameref;//小单位名称
    private String gps_id;//价格方案id
    private String gps_add_num;//最小增量
    private  String gps_min_num;//起订量
    private String goods_img_url;//产品图片

    /*组合促销*/
    private String activityName;//活动名称
    private String activityDesc;//活动描述
    private String activityStock;//活动剩余分数
    private String activityPrice;//活动价格
    private String activityDiscountPrice;//活动优惠价格
    private String pag_goods_num;//组合商品购买数量；

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityStock() {
        return activityStock;
    }

    public void setActivityStock(String activityStock) {
        this.activityStock = activityStock;
    }

    public String getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(String activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getActivityDiscountPrice() {
        return activityDiscountPrice;
    }

    public void setActivityDiscountPrice(String activityDiscountPrice) {
        this.activityDiscountPrice = activityDiscountPrice;
    }

    public String getPag_goods_num() {
        return pag_goods_num;
    }

    public void setPag_goods_num(String pag_goods_num) {
        this.pag_goods_num = pag_goods_num;
    }

    public String getActivityCover() {
        return activityCover;
    }

    public void setActivityCover(String activityCover) {
        this.activityCover = activityCover;
    }

    /*public List<ActionBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<ActionBean> dataList) {
        this.dataList = dataList;
    }*/

    public List<ActionBean> getListData() {
        return listData;
    }

    public void setListData(List<ActionBean> listData) {
        this.listData = listData;
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

    public String getGoods_img_url() {
        return goods_img_url;
    }

    public void setGoods_img_url(String goods_img_url) {
        this.goods_img_url = goods_img_url;
    }

    /*private String goodsId;//	商品id
    private String gbTitle;//	商品标题
    private String goodsStock;//	库存
    private String gbCover;//	商品图片
    private String delivery;//	配送方式
    private String supplierid;//	店家id
    private String standard;//	规格描述
    private String gp_minnum;//	起订量
    private String gp_wholesale_price;//	批发价
    private String gp_wholesale_relation_price;//	箱规批发价
    private String gp_addnum;//	增量
    private String cartnum;//	购物车数量
    private String gpId;//	在跳转商品详情使用
    private String gp_alarm_stock; //商品预警数量

    private String activityId;//商品活动id

    private String activityType;//	活动类型	3 加价购0 普通 3无加减号
    private String goodsDesc; //活动字段

    private String gp_valid_date;//	过期时间
    private String gp_discount;//	折扣率
    private String gp_discount_amount;//	折扣售卖金额
    private String gp_goods_type; //商品类型 1普通 2打折 3赠品 4换购 5特价 6淘汰品
    // 7活动奖品 8 限购 9 套餐 10团购 11 积分商品 12 代购商品 14 临期 15 预售*/



}
