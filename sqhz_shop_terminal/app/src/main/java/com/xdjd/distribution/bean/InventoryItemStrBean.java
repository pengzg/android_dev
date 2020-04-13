package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class InventoryItemStrBean {
    private String cii_price_id;//	商品价格主键
    private String cii_goodsId;//	商品主键
    private String cii_goods_quantity_min;//	小单位数量
    private String cii_goods_price_min;//	小单位价格
//    private String cii_unit_min;//	小单位名
//    private String cii_unitid_max;//	大单位名
    private String cii_goods_quantity_max;//	大单位数量
    private String cii_goods_price_max;//	大单位价格
    private String cii_goods_name;//	商品名
//    private String cii_goods_quantity;//	商品数目
    private String cii_goods_amount;//	商品价格小计
    private String cii_display_quantity;//	显示陈列数量
    private String cii_duitou_quantity;//	堆头数量
//    cii_type	1  堆头  2陈列
    private String cii_create_date;//	生产日期
    private String cii_note;

    public String getCii_note() {
        return cii_note;
    }

    public void setCii_note(String cii_note) {
        this.cii_note = cii_note;
    }

    public String getCii_create_date() {
        return cii_create_date;
    }

    public void setCii_create_date(String cii_create_date) {
        this.cii_create_date = cii_create_date;
    }

    public String getCii_price_id() {
        return cii_price_id;
    }

    public void setCii_price_id(String cii_price_id) {
        this.cii_price_id = cii_price_id;
    }

    public String getCii_goodsId() {
        return cii_goodsId;
    }

    public void setCii_goodsId(String cii_goodsId) {
        this.cii_goodsId = cii_goodsId;
    }

    public String getCii_goods_quantity_min() {
        return cii_goods_quantity_min;
    }

    public void setCii_goods_quantity_min(String cii_goods_quantity_min) {
        this.cii_goods_quantity_min = cii_goods_quantity_min;
    }

    public String getCii_goods_price_min() {
        return cii_goods_price_min;
    }

    public void setCii_goods_price_min(String cii_goods_price_min) {
        this.cii_goods_price_min = cii_goods_price_min;
    }

//    public String getCii_unit_min() {
//        return cii_unit_min;
//    }
//
//    public void setCii_unit_min(String cii_unit_min) {
//        this.cii_unit_min = cii_unit_min;
//    }
//
//    public String getCii_unitid_max() {
//        return cii_unitid_max;
//    }
//
//    public void setCii_unitid_max(String cii_unitid_max) {
//        this.cii_unitid_max = cii_unitid_max;
//    }

    public String getCii_goods_quantity_max() {
        return cii_goods_quantity_max;
    }

    public void setCii_goods_quantity_max(String cii_goods_quantity_max) {
        this.cii_goods_quantity_max = cii_goods_quantity_max;
    }

    public String getCii_goods_price_max() {
        return cii_goods_price_max;
    }

    public void setCii_goods_price_max(String cii_goods_price_max) {
        this.cii_goods_price_max = cii_goods_price_max;
    }

    public String getCii_goods_name() {
        return cii_goods_name;
    }

    public void setCii_goods_name(String cii_goods_name) {
        this.cii_goods_name = cii_goods_name;
    }

    public String getCii_goods_amount() {
        return cii_goods_amount;
    }

    public void setCii_goods_amount(String cii_goods_amount) {
        this.cii_goods_amount = cii_goods_amount;
    }

    public String getCii_display_quantity() {
        return cii_display_quantity;
    }

    public void setCii_display_quantity(String cii_display_quantity) {
        this.cii_display_quantity = cii_display_quantity;
    }

    public String getCii_duitou_quantity() {
        return cii_duitou_quantity;
    }

    public void setCii_duitou_quantity(String cii_duitou_quantity) {
        this.cii_duitou_quantity = cii_duitou_quantity;
    }
}
