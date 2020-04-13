package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DisplayFeeStrBean {

    private String eii_price_id;//	商品价格主键
    private String eii_goodsId;//	商品主键
    private String eii_goods_quantity_min;//	小单位数量
    private String eii_goods_quantity_max;//	大单位数量
    private String eii_goods_price_min;//	小单位价格
    private String eii_goods_price_max;//	大单位价格
    private String eii_unit_min;//	小单位名
    private String eii_unitid_max;//	大单位名
    private String eii_goods_name;//	商品名
    private String eii_goods_quantity;//	商品数目
    private String eii_goods_amount;//	商品价格小计
    private String eii_selltype_id;//	销售类型

    public String getEii_price_id() {
        return eii_price_id;
    }

    public void setEii_price_id(String eii_price_id) {
        this.eii_price_id = eii_price_id;
    }

    public String getEii_goodsId() {
        return eii_goodsId;
    }

    public void setEii_goodsId(String eii_goodsId) {
        this.eii_goodsId = eii_goodsId;
    }

    public String getEii_goods_quantity_min() {
        return eii_goods_quantity_min;
    }

    public void setEii_goods_quantity_min(String eii_goods_quantity_min) {
        this.eii_goods_quantity_min = eii_goods_quantity_min;
    }

    public String getEii_goods_price_min() {
        return eii_goods_price_min;
    }

    public void setEii_goods_price_min(String eii_goods_price_min) {
        this.eii_goods_price_min = eii_goods_price_min;
    }

    public String getEii_unit_min() {
        return eii_unit_min;
    }

    public void setEii_unit_min(String eii_unit_min) {
        this.eii_unit_min = eii_unit_min;
    }

    public String getEii_unitid_max() {
        return eii_unitid_max;
    }

    public void setEii_unitid_max(String eii_unitid_max) {
        this.eii_unitid_max = eii_unitid_max;
    }

    public String getEii_goods_quantity_max() {
        return eii_goods_quantity_max;
    }

    public void setEii_goods_quantity_max(String eii_goods_quantity_max) {
        this.eii_goods_quantity_max = eii_goods_quantity_max;
    }

    public String getEii_goods_price_max() {
        return eii_goods_price_max;
    }

    public void setEii_goods_price_max(String eii_goods_price_max) {
        this.eii_goods_price_max = eii_goods_price_max;
    }

    public String getEii_goods_name() {
        return eii_goods_name;
    }

    public void setEii_goods_name(String eii_goods_name) {
        this.eii_goods_name = eii_goods_name;
    }

    public String getEii_goods_quantity() {
        return eii_goods_quantity;
    }

    public void setEii_goods_quantity(String eii_goods_quantity) {
        this.eii_goods_quantity = eii_goods_quantity;
    }

    public String getEii_goods_amount() {
        return eii_goods_amount;
    }

    public void setEii_goods_amount(String eii_goods_amount) {
        this.eii_goods_amount = eii_goods_amount;
    }

    public String getEii_selltype_id() {
        return eii_selltype_id;
    }

    public void setEii_selltype_id(String eii_selltype_id) {
        this.eii_selltype_id = eii_selltype_id;
    }
}
