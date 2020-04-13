package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PrintOrderBean extends BaseBean {

    private String om_orderdate;//	日期
    private String om_code;//	订单编号
    private String om_delivery_amount;//	发货金额

    private String sign_url_nameref;//签名图片地址

    private List<PrintOrderBean> dataList;//	订单商品列表

    private String od_id;//	订单明细id
    private String od_goods_name;//	商品名称
//    private String od_delivery_num_min;//	小单位数量
//    private String od_delivery_num_max;//	大单位数量

    private String od_goods_num_max;
    private String od_goods_num_min;

    private String od_goods_unitname_min;//	小单位描述
    private String od_goods_unitname_max;//	大单位描述
    private String gg_model = "";//	商品规格
    private String od_price_min;//	商品小单位价格
    private String od_price_max;//	商品大单位价格
    private String od_real_total;//	商品总价格

    private String gg_international_code_max;// 大单位
    private String gg_international_code_min = "";//小单位

    private String od_goods_state_nameref;//商品状态
    private int stats = 0;//6是订单已发货收款状态

    private String od_unit_num;

    private String od_delivery_num;//	实际 发货数量
    private String od_delivery_amount;//	实际发货金额
    private String od_delivery_num_min;//	小单位发货数量
    private String od_delivery_num_max;//	大单位发货数量

    //    private String bi_id;//	主键
//    private String bi_name;//	付款方式

    private String xjAmount = "0.00";//	现金
    private String yskAmount = "0.00";//应收款
    private String skAmount = "0.00";//	刷卡
    private String yhAmount = "0.00";//	优惠
    private String ysAmount = "0.00";//  预收款

    private String sfkAmount;//	实付款


    public String getSign_url_nameref() {
        return sign_url_nameref;
    }

    public void setSign_url_nameref(String sign_url_nameref) {
        this.sign_url_nameref = sign_url_nameref;
    }

    public int getStats() {
        return stats;
    }

    public void setStats(int stats) {
        this.stats = stats;
    }

    public String getOd_goods_state_nameref() {
        return od_goods_state_nameref;
    }

    public void setOd_goods_state_nameref(String od_goods_state_nameref) {
        this.od_goods_state_nameref = od_goods_state_nameref;
    }

    public String getOd_delivery_num() {
        return od_delivery_num;
    }

    public void setOd_delivery_num(String od_delivery_num) {
        this.od_delivery_num = od_delivery_num;
    }

    public String getOd_delivery_amount() {
        return od_delivery_amount;
    }

    public void setOd_delivery_amount(String od_delivery_amount) {
        this.od_delivery_amount = od_delivery_amount;
    }

    public String getOd_delivery_num_min() {
        return od_delivery_num_min;
    }

    public void setOd_delivery_num_min(String od_delivery_num_min) {
        this.od_delivery_num_min = od_delivery_num_min;
    }

    public String getOd_delivery_num_max() {
        return od_delivery_num_max;
    }

    public void setOd_delivery_num_max(String od_delivery_num_max) {
        this.od_delivery_num_max = od_delivery_num_max;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
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

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getYskAmount() {
        return yskAmount;
    }

    public void setYskAmount(String yskAmount) {
        this.yskAmount = yskAmount;
    }

    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public String getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(String yhAmount) {
        this.yhAmount = yhAmount;
    }

    public String getSfkAmount() {
        return sfkAmount;
    }

    public void setSfkAmount(String sfkAmount) {
        this.sfkAmount = sfkAmount;
    }

    public String getOd_goods_num_max() {
        return od_goods_num_max;
    }

    public void setOd_goods_num_max(String od_goods_num_max) {
        this.od_goods_num_max = od_goods_num_max;
    }

    public String getOd_goods_num_min() {
        return od_goods_num_min;
    }

    public void setOd_goods_num_min(String od_goods_num_min) {
        this.od_goods_num_min = od_goods_num_min;
    }

    public String getOm_orderdate() {
        return om_orderdate;
    }

    public void setOm_orderdate(String om_orderdate) {
        this.om_orderdate = om_orderdate;
    }

    public String getOm_code() {
        return om_code;
    }

    public void setOm_code(String om_code) {
        this.om_code = om_code;
    }

    public String getOm_delivery_amount() {
        return om_delivery_amount;
    }

    public void setOm_delivery_amount(String om_delivery_amount) {
        this.om_delivery_amount = om_delivery_amount;
    }

    public List<PrintOrderBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PrintOrderBean> dataList) {
        this.dataList = dataList;
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
