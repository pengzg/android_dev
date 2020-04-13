package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/12.
 */

public class OrderDetailBean extends BaseBean implements Serializable {
    private String om_ordercode;//订单编号
    private String om_id;//订单id
    private String om_stats;//订单状态
    private String om_order_amount;//订单金额
    private String om_stats_nameref;//订单状态名称
    private String om_orderdate;//订单日期
    private String om_goods_num;//订货数量
    private String om_delivery_num;//发货数量
    private String om_goods_amount;//商品金额
    private String om_discount_amount;//优惠金额
    private String om_freight_amount;//运费
    private String om_sett_amount;//结算金额
    private String om_delivery_amount;//发货金额
    private String om_remarks;//备注
    private String om_customer_name;//客户名称
    private String om_mobile;//客户电话
    private String om_address;//收货地址
    private String om_orgid_nameref;//配送店铺
    private String om_orgphone;//配送店铺电话
    private String 	om_version;//版本号
    private String om_pay_type_nameref;//支付方式
    private List<OrderGoodsDetailBean> listData;//订单明细
    private List<ButtonListBean> buttonList;//btn按钮

    public String getOm_order_amount() {
        return om_order_amount;
    }

    public void setOm_order_amount(String om_order_amount) {
        this.om_order_amount = om_order_amount;
    }

    public String getOm_pay_type_nameref() {
        return om_pay_type_nameref;
    }

    public void setOm_pay_type_nameref(String om_pay_type_nameref) {
        this.om_pay_type_nameref = om_pay_type_nameref;
    }

    public String getOm_ordercode() {
        return om_ordercode;
    }

    public void setOm_ordercode(String om_ordercode) {
        this.om_ordercode = om_ordercode;
    }

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public String getOm_stats() {
        return om_stats;
    }

    public void setOm_stats(String om_stats) {
        this.om_stats = om_stats;
    }

    public String getOm_stats_nameref() {
        return om_stats_nameref;
    }

    public void setOm_stats_nameref(String om_stats_nameref) {
        this.om_stats_nameref = om_stats_nameref;
    }

    public String getOm_orderdate() {
        return om_orderdate;
    }

    public void setOm_orderdate(String om_orderdate) {
        this.om_orderdate = om_orderdate;
    }

    public String getOm_goods_num() {
        return om_goods_num;
    }

    public void setOm_goods_num(String om_goods_num) {
        this.om_goods_num = om_goods_num;
    }

    public String getOm_delivery_num() {
        return om_delivery_num;
    }

    public void setOm_delivery_num(String om_delivery_num) {
        this.om_delivery_num = om_delivery_num;
    }

    public String getOm_goods_amount() {
        return om_goods_amount;
    }

    public void setOm_goods_amount(String om_goods_amount) {
        this.om_goods_amount = om_goods_amount;
    }

    public String getOm_discount_amount() {
        return om_discount_amount;
    }

    public void setOm_discount_amount(String om_discount_amount) {
        this.om_discount_amount = om_discount_amount;
    }

    public String getOm_freight_amount() {
        return om_freight_amount;
    }

    public void setOm_freight_amount(String om_freight_amount) {
        this.om_freight_amount = om_freight_amount;
    }

    public String getOm_sett_amount() {
        return om_sett_amount;
    }

    public void setOm_sett_amount(String om_sett_amount) {
        this.om_sett_amount = om_sett_amount;
    }

    public String getOm_delivery_amount() {
        return om_delivery_amount;
    }

    public void setOm_delivery_amount(String om_delivery_amount) {
        this.om_delivery_amount = om_delivery_amount;
    }

    public String getOm_remarks() {
        return om_remarks;
    }

    public void setOm_remarks(String om_remarks) {
        this.om_remarks = om_remarks;
    }

    public String getOm_customer_name() {
        return om_customer_name;
    }

    public void setOm_customer_name(String om_customer_name) {
        this.om_customer_name = om_customer_name;
    }

    public String getOm_mobile() {
        return om_mobile;
    }

    public void setOm_mobile(String om_mobile) {
        this.om_mobile = om_mobile;
    }

    public String getOm_address() {
        return om_address;
    }

    public void setOm_address(String om_address) {
        this.om_address = om_address;
    }

    public List<OrderGoodsDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<OrderGoodsDetailBean> listData) {
        this.listData = listData;
    }

    public String getOm_orgid_nameref() {
        return om_orgid_nameref;
    }

    public void setOm_orgid_nameref(String om_orgid_nameref) {
        this.om_orgid_nameref = om_orgid_nameref;
    }

    public String getOm_orgphone() {
        return om_orgphone;
    }

    public void setOm_orgphone(String om_orgphone) {
        this.om_orgphone = om_orgphone;
    }

    public String getOm_version() {
        return om_version;
    }

    public void setOm_version(String om_version) {
        this.om_version = om_version;
    }

    public List<ButtonListBean> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<ButtonListBean> buttonList) {
        this.buttonList = buttonList;
    }
}
