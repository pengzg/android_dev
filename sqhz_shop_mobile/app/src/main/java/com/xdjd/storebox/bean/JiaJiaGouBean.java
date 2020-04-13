package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 加价购商品bean
 * Created by lijipei on 2016/12/16.
 */

public class JiaJiaGouBean extends BaseBean{

    private List<JiaJiaGouBean> listData;

    private String wpg_goods_gift_id;//	商品id
    private String gb_title;//	商品标题
    private String wpg_goods_stock;//	活动商品库存
    private String shopId;//	店家id
    private String cover;//	商品图片
    private String wpa_activity_name;//	活动名
    private String wpa_id;//	活动id
    private String wpg_gift_num;//	活动数量
    private String gp_wholesale_price;//	批发价
    private String gp_wholesale_relation_price;//	箱规批发价
    private String wpg_goods_amount;//	活动价格
    private double wpg_min_amount;//	参加活动最小金额
    private String wpg_sale_num;//	销售数量
    private String wpg_id;//	活动详情id
    private int leftNum;//	剩下的数量 	大于0能选 否则 不能选

    public int getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(int leftNum) {
        this.leftNum = leftNum;
    }

    public double getWpg_min_amount() {
        return wpg_min_amount;
    }

    public void setWpg_min_amount(double wpg_min_amount) {
        this.wpg_min_amount = wpg_min_amount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<JiaJiaGouBean> getListData() {
        return listData;
    }

    public void setListData(List<JiaJiaGouBean> listData) {
        this.listData = listData;
    }

    public String getWpg_goods_gift_id() {
        return wpg_goods_gift_id;
    }

    public void setWpg_goods_gift_id(String wpg_goods_gift_id) {
        this.wpg_goods_gift_id = wpg_goods_gift_id;
    }

    public String getGb_title() {
        return gb_title;
    }

    public void setGb_title(String gb_title) {
        this.gb_title = gb_title;
    }

    public String getWpg_goods_stock() {
        return wpg_goods_stock;
    }

    public void setWpg_goods_stock(String wpg_goods_stock) {
        this.wpg_goods_stock = wpg_goods_stock;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getWpa_activity_name() {
        return wpa_activity_name;
    }

    public void setWpa_activity_name(String wpa_activity_name) {
        this.wpa_activity_name = wpa_activity_name;
    }

    public String getWpa_id() {
        return wpa_id;
    }

    public void setWpa_id(String wpa_id) {
        this.wpa_id = wpa_id;
    }

    public String getWpg_gift_num() {
        return wpg_gift_num;
    }

    public void setWpg_gift_num(String wpg_gift_num) {
        this.wpg_gift_num = wpg_gift_num;
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

    public String getWpg_goods_amount() {
        return wpg_goods_amount;
    }

    public void setWpg_goods_amount(String wpg_goods_amount) {
        this.wpg_goods_amount = wpg_goods_amount;
    }

    public String getWpg_sale_num() {
        return wpg_sale_num;
    }

    public void setWpg_sale_num(String wpg_sale_num) {
        this.wpg_sale_num = wpg_sale_num;
    }

    public String getWpg_id() {
        return wpg_id;
    }

    public void setWpg_id(String wpg_id) {
        this.wpg_id = wpg_id;
    }
}
