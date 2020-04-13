package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/25
 *     desc   : 订单申报生成提交结算bean
 *     version: 1.0
 * </pre>
 */

public class OrderDeclareStrBean extends BaseBean implements Serializable {


    private String om_deliverydate;//	订单发货日期	Y
    private String om_storeid;//	发货仓库	Y
    private String om_ordertype;//	订单类型	Y
    private String om_ischages;//	是否变价	N	Y变价 N未变货
    private String om_remarks;//	订单备注	Y
    private List<OrderDeclareStrBean> orderDetailStr;//	订单详情字段	Y

    private String od_goods_price_id;//	商品价格主键
    private String od_pricetype;//	商品类型
    private String od_goods_num_min;//	小单位购买数量
    private String od_goods_num_max;//	大单位购买数量
    private String od_price_min;//	小单位价格
    private String od_price_max;//	大单位价格
    private String od_note;//	商品备注
    private String od_price_strategyid;//	价格方案id
    private String source_id;//	L来源id
    private String goodsStatus;//	商品状态

    public String getOm_deliverydate() {
        return om_deliverydate;
    }

    public void setOm_deliverydate(String om_deliverydate) {
        this.om_deliverydate = om_deliverydate;
    }

    public String getOm_storeid() {
        return om_storeid;
    }

    public void setOm_storeid(String om_storeid) {
        this.om_storeid = om_storeid;
    }

    public String getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(String om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getOm_ischages() {
        return om_ischages;
    }

    public void setOm_ischages(String om_ischages) {
        this.om_ischages = om_ischages;
    }

    public String getOm_remarks() {
        return om_remarks;
    }

    public void setOm_remarks(String om_remarks) {
        this.om_remarks = om_remarks;
    }

    public List<OrderDeclareStrBean> getOrderDetailStr() {
        return orderDetailStr;
    }

    public void setOrderDetailStr(List<OrderDeclareStrBean> orderDetailStr) {
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

    public String getOd_price_strategyid() {
        return od_price_strategyid;
    }

    public void setOd_price_strategyid(String od_price_strategyid) {
        this.od_price_strategyid = od_price_strategyid;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }
}
