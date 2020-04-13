package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单结算参数拼接对象
 */
public class OrderDetailStrBean extends BaseBean implements Serializable{

    private String om_ordertype;//	订单类型
    List<OrderDetailStrBean> orderDetailStr;

    private String od_goods_price_id;//	商品价格主键
    private String od_pricetype;//	商品类型
    private String od_goods_num_min;//	小单位购买数量
    private String od_goods_num_max;//	大单位购买数量
    private String od_price_min;//	小单位价格
    private String od_price_max;//	大单位价格
    private String od_note;//	商品备注

    private String od_price_strategyid;//	价格方案id
    private String source_id;//	L来源id

    private String goodstype;//商品类型,是否是赠品

    private String goodsStatus;//	商品状态

    private String od_id;//	订单明细id
    private String goods_num_min;//	小单位购买数量
    private String goods_num_max;//	大单位购买数量
    //配送任务生成支付单据时用到的字段
    private String om_id;//	订单id
    private String om_version;//	订单版本号

    //----结算界面使用的参数-----
    private String payType;//	支付方式	Y	1 混合收款 2优先现款 3优先预收款 4优先应收款
    private String skType;//	刷卡方式	Y	1不使用刷卡2非系统集成刷卡3银联商务4支付宝支付5微信支付6第三方
    private String skAmount;//	刷卡金额	Y
    private String yhAmount;//	优惠金额	Y
    private String xjAmount;//	现金金额	Y
    private String yeAmount;//  余额金额
    private String ysAmount;//  应收金额


    public String getGoodstype() {
        return goodstype;
    }

    public void setGoodstype(String goodstype) {
        this.goodstype = goodstype;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getYeAmount() {
        return yeAmount;
    }

    public void setYeAmount(String yeAmount) {
        this.yeAmount = yeAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSkType() {
        return skType;
    }

    public void setSkType(String skType) {
        this.skType = skType;
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

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(String om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getOd_price_strategyid() {
        return od_price_strategyid;
    }

    public void setOd_price_strategyid(String od_price_strategyid) {
        this.od_price_strategyid = od_price_strategyid;
    }

    public String getOd_id() {
        return od_id;
    }

    public void setOd_id(String od_id) {
        this.od_id = od_id;
    }

    public String getGoods_num_min() {
        return goods_num_min;
    }

    public void setGoods_num_min(String goods_num_min) {
        this.goods_num_min = goods_num_min;
    }

    public String getGoods_num_max() {
        return goods_num_max;
    }

    public void setGoods_num_max(String goods_num_max) {
        this.goods_num_max = goods_num_max;
    }

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public String getOm_version() {
        return om_version;
    }

    public void setOm_version(String om_version) {
        this.om_version = om_version;
    }

    public List<OrderDetailStrBean> getOrderDetailStr() {
        return orderDetailStr;
    }

    public void setOrderDetailStr(List<OrderDetailStrBean> orderDetailStr) {
        this.orderDetailStr = orderDetailStr;
    }

    public String getOd_goods_price_id() {
        return od_goods_price_id;
    }

    public void setOd_goods_price_id(String od_goods_price_id) {
        this.od_goods_price_id = od_goods_price_id;
    }

    public String getOd_pricetype() {
        return od_pricetype;
    }

    public void setOd_pricetype(String od_pricetype) {
        this.od_pricetype = od_pricetype;
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

    public String getOd_note() {
        return od_note;
    }

    public void setOd_note(String od_note) {
        this.od_note = od_note;
    }
}
