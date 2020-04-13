package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/17
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DisplayInDetailBean extends BaseBean implements Serializable {

    private String eim_code;//	Code
    private String customerName;//	客户名称
    private String amount;//	金额
    private String eim_billdate;//	日期
    private String om_id;//	订单id
    private List<DisplayInDetailBean> dataList;//	详情

    private String eii_price_id;//	商品价格主键
    private String eii_goodsId;//	商品主键
    private String eii_goods_quantity_min="0";//	小单位数量
    private String eii_goods_quantity_max="0";//	大单位数量
    private String eii_goods_price_min="0.00";//	小单位价格
    private String eii_goods_price_max="0.00";//	大单位价格
    private String eii_unit_min="";//	小单位名
    private String eii_unit_max="";//	大单位名
    private String eii_goods_name="";//	商品名
    private String eii_goods_quantity;//	商品数目
    private String eii_goods_amount;//	商品价格小计
    private String eii_selltype_id;//	销售类型
    private String gg_model;//	商品规格
    private String eii_unit_num;//	单位换算
    private String eii_goods_state;//	商品状态
    private String eii_goods_state_nameref;//	商品状态


    public String getEii_goods_price_max() {
        return eii_goods_price_max;
    }

    public void setEii_goods_price_max(String eii_goods_price_max) {
        this.eii_goods_price_max = eii_goods_price_max;
    }

    public String getEii_unit_min() {
        return eii_unit_min;
    }

    public void setEii_unit_min(String eii_unit_min) {
        this.eii_unit_min = eii_unit_min;
    }

    public String getEii_unit_max() {
        return eii_unit_max;
    }

    public void setEii_unit_max(String eii_unit_max) {
        this.eii_unit_max = eii_unit_max;
    }

    public String getEii_goods_quantity_max() {
        return eii_goods_quantity_max;
    }

    public void setEii_goods_quantity_max(String eii_goods_quantity_max) {
        this.eii_goods_quantity_max = eii_goods_quantity_max;
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

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getEii_unit_num() {
        return eii_unit_num;
    }

    public void setEii_unit_num(String eii_unit_num) {
        this.eii_unit_num = eii_unit_num;
    }

    public String getEii_goods_state() {
        return eii_goods_state;
    }

    public void setEii_goods_state(String eii_goods_state) {
        this.eii_goods_state = eii_goods_state;
    }

    public String getEii_goods_state_nameref() {
        return eii_goods_state_nameref;
    }

    public void setEii_goods_state_nameref(String eii_goods_state_nameref) {
        this.eii_goods_state_nameref = eii_goods_state_nameref;
    }

    public String getEim_code() {
        return eim_code;
    }

    public void setEim_code(String eim_code) {
        this.eim_code = eim_code;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEim_billdate() {
        return eim_billdate;
    }

    public void setEim_billdate(String eim_billdate) {
        this.eim_billdate = eim_billdate;
    }

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public List<DisplayInDetailBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DisplayInDetailBean> dataList) {
        this.dataList = dataList;
    }

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
}
