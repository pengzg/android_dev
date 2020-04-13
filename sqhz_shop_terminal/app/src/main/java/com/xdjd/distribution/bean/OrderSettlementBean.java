package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/20
 *     desc   : 订单结算参数展示bean
 *     version: 1.0
 * </pre>
 */

public class OrderSettlementBean extends BaseBean implements Serializable{

    private int om_ordertype;//	订单类型
    private List<OrderSettlementBean> listData;//	商品列表
    private String order_type_amount;  //订单类型总金额

    private String od_goods_name;//	商品名称
    private String od_goods_num_min;//	小单位数量
    private String od_goods_num_max;//	大单位数量
    private String od_goods_unitname_min;//	小单位描述
    private String od_goods_unitname_max;//	大单位描述
    private String od_price_min = "0.00";//	商品小单位价格
    private String od_price_max = "0.00";//	商品大单位价格
    private String od_real_total;//	商品总价格
    private String od_unit_num;//	单位换算
    private String goodsType;//	商品类型	1为赠品 2 为普通

    private String od_goods_state;//	商品状态
    private String od_goods_state_nameref;//	商品状态

    private String gg_international_code_max = "";// 大单位
    private String gg_international_code_min = "";//小单位

    //------生产的活动数据参数----
    /**
     * 总的商品列表--包含订单、处理单、退货单等
     */
    private List<OrderSettlementBean> listBuildData;
    private String total_amount;//	所有订单合计总金额


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

    public String getOrder_type_amount() {
        return order_type_amount;
    }

    public void setOrder_type_amount(String order_type_amount) {
        this.order_type_amount = order_type_amount;
    }

    public int getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(int om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public List<OrderSettlementBean> getListBuildData() {
        return listBuildData;
    }

    public void setListBuildData(List<OrderSettlementBean> listBuildData) {
        this.listBuildData = listBuildData;
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

    public List<OrderSettlementBean> getListData() {
        return listData;
    }

    public void setListData(List<OrderSettlementBean> listData) {
        this.listData = listData;
    }

    public String getOd_goods_name() {
        return od_goods_name;
    }

    public void setOd_goods_name(String od_goods_name) {
        this.od_goods_name = od_goods_name;
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

    public String getOd_unit_num() {
        return od_unit_num;
    }

    public void setOd_unit_num(String od_unit_num) {
        this.od_unit_num = od_unit_num;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
