package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class InventoryDetailBean extends BaseBean {

    private List<InventoryDetailBean> dataList;//	盘点列表

    private String cii_goods_amount = "";//	商品金额
    private String cii_goods_name = "";//	商品名
    private String cii_goods_price_max = "";//	大单位价格
    private String cii_goods_price_min = "";//	小单位价格
    private String cii_goods_quantity = "";//	商品总数
    private String cii_goods_quantity_max = "";//	大单位数量
    private String cii_goods_quantity_min = "";//	小单位数量
    private String cii_unit_num = "";//	单位换算
    private String cii_unit_max = "";//	大单位名
    private String cii_unit_min = "";//	小单位名
    private String cii_create_date = "";//	生产日期
    private String cii_display_quantity = "0";//	陈列数量
    private String cii_duitou_quantity = "0";//	堆头数量

    public List<InventoryDetailBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<InventoryDetailBean> dataList) {
        this.dataList = dataList;
    }

    public String getCii_goods_amount() {
        return cii_goods_amount;
    }

    public void setCii_goods_amount(String cii_goods_amount) {
        this.cii_goods_amount = cii_goods_amount;
    }

    public String getCii_goods_name() {
        return cii_goods_name;
    }

    public void setCii_goods_name(String cii_goods_name) {
        this.cii_goods_name = cii_goods_name;
    }

    public String getCii_goods_price_max() {
        return cii_goods_price_max;
    }

    public void setCii_goods_price_max(String cii_goods_price_max) {
        this.cii_goods_price_max = cii_goods_price_max;
    }

    public String getCii_goods_price_min() {
        return cii_goods_price_min;
    }

    public void setCii_goods_price_min(String cii_goods_price_min) {
        this.cii_goods_price_min = cii_goods_price_min;
    }

    public String getCii_goods_quantity() {
        return cii_goods_quantity;
    }

    public void setCii_goods_quantity(String cii_goods_quantity) {
        this.cii_goods_quantity = cii_goods_quantity;
    }

    public String getCii_goods_quantity_max() {
        return cii_goods_quantity_max;
    }

    public void setCii_goods_quantity_max(String cii_goods_quantity_max) {
        this.cii_goods_quantity_max = cii_goods_quantity_max;
    }

    public String getCii_goods_quantity_min() {
        return cii_goods_quantity_min;
    }

    public void setCii_goods_quantity_min(String cii_goods_quantity_min) {
        this.cii_goods_quantity_min = cii_goods_quantity_min;
    }

    public String getCii_unit_num() {
        return cii_unit_num;
    }

    public void setCii_unit_num(String cii_unit_num) {
        this.cii_unit_num = cii_unit_num;
    }

    public String getCii_unit_max() {
        return cii_unit_max;
    }

    public void setCii_unit_max(String cii_unit_max) {
        this.cii_unit_max = cii_unit_max;
    }

    public String getCii_unit_min() {
        return cii_unit_min;
    }

    public void setCii_unit_min(String cii_unit_min) {
        this.cii_unit_min = cii_unit_min;
    }

    public String getCii_create_date() {
        return cii_create_date;
    }

    public void setCii_create_date(String cii_create_date) {
        this.cii_create_date = cii_create_date;
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
