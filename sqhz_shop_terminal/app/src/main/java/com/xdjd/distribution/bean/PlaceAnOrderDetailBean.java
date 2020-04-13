package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PlaceAnOrderDetailBean extends BaseBean implements Serializable{

    private String total_amount;//	总金额
    private String no_delivery_amount;//	未发货金额
    private String oa_version;//		版本
    private String flag;//		1可以取消 2 不可以取消
    private String oa_customername;//		客户名称
    private String oa_mobile;//		客户电话
    private String oa_address;//		客户收货地址
    private String oa_contacts_name;
    private String oa_state_nameref;//订单状态
    private String discountAmount;//	优惠金额
    private String skAmount;//	刷卡金额
    private String yuskAmount;//	预收款
    private String xjAmount;//	现金
    private String sfkAmount;//	实付款金额
    private List<PlaceAnOrderDetailBean> listData;//		订单数据集合

    private String oad_goods_name;//		商品名
    private String oad_goods_unitname_min;//		小单位名称
    private String oad_goods_unitname_max;//		大单位名称
    private String oad_unit;//		单位基准 1 小单位 2 大单位
    private String oad_unit_num;//		单位数量
    private String oad_total_amount = "0.00";//		金额
    private String oad_goods_num;//		商品数量
    private int order_surplus_num;//		未发货数量
    private String order_surplus_total;//		未发货金额


    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public String getYuskAmount() {
        return yuskAmount;
    }

    public void setYuskAmount(String yuskAmount) {
        this.yuskAmount = yuskAmount;
    }

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getSfkAmount() {
        return sfkAmount;
    }

    public void setSfkAmount(String sfkAmount) {
        this.sfkAmount = sfkAmount;
    }

    public String getOa_contacts_name() {
        return oa_contacts_name;
    }

    public void setOa_contacts_name(String oa_contacts_name) {
        this.oa_contacts_name = oa_contacts_name;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOa_state_nameref() {
        return oa_state_nameref;
    }

    public void setOa_state_nameref(String oa_state_nameref) {
        this.oa_state_nameref = oa_state_nameref;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getNo_delivery_amount() {
        return no_delivery_amount;
    }

    public void setNo_delivery_amount(String no_delivery_amount) {
        this.no_delivery_amount = no_delivery_amount;
    }

    public String getOa_version() {
        return oa_version;
    }

    public void setOa_version(String oa_version) {
        this.oa_version = oa_version;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getOa_customername() {
        return oa_customername;
    }

    public void setOa_customername(String oa_customername) {
        this.oa_customername = oa_customername;
    }

    public String getOa_mobile() {
        return oa_mobile;
    }

    public void setOa_mobile(String oa_mobile) {
        this.oa_mobile = oa_mobile;
    }

    public String getOa_address() {
        return oa_address;
    }

    public void setOa_address(String oa_address) {
        this.oa_address = oa_address;
    }

    public int getOrder_surplus_num() {
        return order_surplus_num;
    }

    public void setOrder_surplus_num(int order_surplus_num) {
        this.order_surplus_num = order_surplus_num;
    }

    public String getOrder_surplus_total() {
        return order_surplus_total;
    }

    public void setOrder_surplus_total(String order_surplus_total) {
        this.order_surplus_total = order_surplus_total;
    }

    public String getOad_total_amount() {
        return oad_total_amount;
    }

    public void setOad_total_amount(String oad_total_amount) {
        this.oad_total_amount = oad_total_amount;
    }

    public String getOad_goods_num() {
        return oad_goods_num;
    }

    public void setOad_goods_num(String oad_goods_num) {
        this.oad_goods_num = oad_goods_num;
    }

    public List<PlaceAnOrderDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<PlaceAnOrderDetailBean> listData) {
        this.listData = listData;
    }

    public String getOad_goods_name() {
        return oad_goods_name;
    }

    public void setOad_goods_name(String oad_goods_name) {
        this.oad_goods_name = oad_goods_name;
    }

    public String getOad_goods_unitname_min() {
        return oad_goods_unitname_min;
    }

    public void setOad_goods_unitname_min(String oad_goods_unitname_min) {
        this.oad_goods_unitname_min = oad_goods_unitname_min;
    }

    public String getOad_goods_unitname_max() {
        return oad_goods_unitname_max;
    }

    public void setOad_goods_unitname_max(String oad_goods_unitname_max) {
        this.oad_goods_unitname_max = oad_goods_unitname_max;
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

}
