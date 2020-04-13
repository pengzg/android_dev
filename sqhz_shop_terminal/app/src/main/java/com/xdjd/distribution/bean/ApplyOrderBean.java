package com.xdjd.distribution.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/9/21.
 */

public class ApplyOrderBean extends BaseBean {

    private List<ApplyOrderBean> listData;//
    private String oa_applycode;//		订单编号
    private String oa_remarks;//		备注
    private String oa_id;//		订单id
    private String oa_applydate;//		申请日期


    private String oad_goods_id;//		商品id
    private String oad_goods_name;//		商品名称
    private String oad_goods_price_id;//		价格id
    private String oad_goods_unitname_max;//		大单位名称
    private String oad_goods_unitname_min;//		小单位名称
    private String oad_id;//		明细id
    private String oad_total_amount;//		小计
    private String oad_unit = "1";//		单位基准 1 小单位 2 大单位
    private String oad_unit_num;//		单位换算数量
    private String oad_price_min;//		小单位价格
    private String oad_price_max;//		大单位价格
    private String order_surplus_num;//		剩余未发货数量
    private String order_surplus_total;//		剩余未发货金额
    private String ggs_stock;//		车库存
    private String gg_model;//		规格

    //-------自定义-------
    private String goods_num_desc;//      数量描述
    private String num_min;//		发货小单位数量
    private String num_max;//		发货大单位数量

    public String getGoods_num_desc() {
        return goods_num_desc;
    }

    public void setGoods_num_desc(String goods_num_desc) {
        this.goods_num_desc = goods_num_desc;
    }

    public String getNum_min() {
        return num_min;
    }

    public void setNum_min(String num_min) {
        this.num_min = num_min;
    }

    public String getNum_max() {
        return num_max;
    }

    public void setNum_max(String num_max) {
        this.num_max = num_max;
    }

    public String getOrder_surplus_num() {
        return order_surplus_num;
    }

    public void setOrder_surplus_num(String order_surplus_num) {
        this.order_surplus_num = order_surplus_num;
    }

    public String getOrder_surplus_total() {
        return order_surplus_total;
    }

    public void setOrder_surplus_total(String order_surplus_total) {
        this.order_surplus_total = order_surplus_total;
    }

    public String getGgs_stock() {
        return ggs_stock;
    }

    public void setGgs_stock(String ggs_stock) {
        this.ggs_stock = ggs_stock;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public List<ApplyOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<ApplyOrderBean> listData) {
        this.listData = listData;
    }

    public String getOa_applycode() {
        return oa_applycode;
    }

    public void setOa_applycode(String oa_applycode) {
        this.oa_applycode = oa_applycode;
    }

    public String getOa_remarks() {
        return oa_remarks;
    }

    public void setOa_remarks(String oa_remarks) {
        this.oa_remarks = oa_remarks;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getOa_applydate() {
        return oa_applydate;
    }

    public void setOa_applydate(String oa_applydate) {
        this.oa_applydate = oa_applydate;
    }

    public String getOad_goods_id() {
        return oad_goods_id;
    }

    public void setOad_goods_id(String oad_goods_id) {
        this.oad_goods_id = oad_goods_id;
    }

    public String getOad_goods_name() {
        return oad_goods_name;
    }

    public void setOad_goods_name(String oad_goods_name) {
        this.oad_goods_name = oad_goods_name;
    }

    public String getOad_goods_price_id() {
        return oad_goods_price_id;
    }

    public void setOad_goods_price_id(String oad_goods_price_id) {
        this.oad_goods_price_id = oad_goods_price_id;
    }

    public String getOad_goods_unitname_max() {
        return oad_goods_unitname_max;
    }

    public void setOad_goods_unitname_max(String oad_goods_unitname_max) {
        this.oad_goods_unitname_max = oad_goods_unitname_max;
    }

    public String getOad_goods_unitname_min() {
        return oad_goods_unitname_min;
    }

    public void setOad_goods_unitname_min(String oad_goods_unitname_min) {
        this.oad_goods_unitname_min = oad_goods_unitname_min;
    }

    public String getOad_id() {
        return oad_id;
    }

    public void setOad_id(String oad_id) {
        this.oad_id = oad_id;
    }

    public String getOad_total_amount() {
        return oad_total_amount;
    }

    public void setOad_total_amount(String oad_total_amount) {
        this.oad_total_amount = oad_total_amount;
    }

    public String getOad_unit() {
        return oad_unit;
    }

    public void setOad_unit(String oad_unit) {
        this.oad_unit = oad_unit;
    }

    public String getOad_unit_num() {
        return oad_unit_num;
    }

    public void setOad_unit_num(String oad_unit_num) {
        this.oad_unit_num = oad_unit_num;
    }

    public String getOad_price_min() {
        return oad_price_min;
    }

    public void setOad_price_min(String oad_price_min) {
        this.oad_price_min = oad_price_min;
    }

    public String getOad_price_max() {
        return oad_price_max;
    }

    public void setOad_price_max(String oad_price_max) {
        this.oad_price_max = oad_price_max;
    }
}
