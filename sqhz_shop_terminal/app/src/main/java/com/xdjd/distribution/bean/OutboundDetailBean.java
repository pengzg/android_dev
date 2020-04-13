package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OutboundDetailBean extends BaseBean {

    private String xjAmount;//	现金
    private String yskAmount;//	应收
    private String skAmount;//	刷卡
    private String yhAmount;//	优惠
    private String ysAmount;//	预收款(余额)

    private String sfkAmount;//实付款

    private String om_id;//	订单id

    private String eim_source;//	来源  	1订单
    private String eim_source_code;//	关联单据号

    private String customerName="";//	名称
    private String customerid_contacts_name="";//	联系人
    private String customerid_contacts_mobile="";//	电话
    private String customerid_address="";//	地址
    private String eim_type="";//	类型	208陈列
    private String sign_url_nameref;//打印签名图片
    private List<OutboundDetailBean> dataList;//	详情

    private String eii_price_id;//	商品价格主键
    private String eii_goodsId;//	商品主键
    private String eii_goods_quantity_min="0";//	小单位数量
    private String eii_goods_price_min="0.00";//	小单位价格
    private String eii_unit_min;//	小单位名
    private String eii_goods_quantity_max="0";//	大单位数量
    private String eii_goods_price_max="0.00";//	大单位价格
    private String eii_goods_name;//	商品名
    private String eii_goods_quantity;//	商品数目
    private String eii_goods_amount;//	商品价格小计
    private String eii_selltype_id;//	销售类型

    private String eii_unit_max="";//	大单位名
    private String gg_model = "";//	商品规格
    private String eii_unit_num;//	单位换算

    private String gg_international_code_max = "";// 大单位
    private String gg_international_code_min = "";//小单位

    private String eim_id;//	出库id
    private String eim_version;//	版本号

    private String is_cancel;//	是否可以撤销	Y是N否
    private String om_source;//	订单来源	订单来源 1 客户商城下单  2 终端申请 3 车销  4 库管下单  5 后台下单

    private int om_ordertype;//	订单类型	订单类型 1 普通 2 处理 3 退货 4 换货 5 还货

    private String eii_goods_state;//	商品状态	商品状态 1 正 2 临 3残 4过
    private String eii_goods_state_nameref;//	商品状态


    public String getSign_url_nameref() {
        return sign_url_nameref;
    }

    public void setSign_url_nameref(String sign_url_nameref) {
        this.sign_url_nameref = sign_url_nameref;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerid_contacts_name() {
        return customerid_contacts_name;
    }

    public void setCustomerid_contacts_name(String customerid_contacts_name) {
        this.customerid_contacts_name = customerid_contacts_name;
    }

    public String getCustomerid_contacts_mobile() {
        return customerid_contacts_mobile;
    }

    public void setCustomerid_contacts_mobile(String customerid_contacts_mobile) {
        this.customerid_contacts_mobile = customerid_contacts_mobile;
    }

    public String getCustomerid_address() {
        return customerid_address;
    }

    public void setCustomerid_address(String customerid_address) {
        this.customerid_address = customerid_address;
    }

    public String getEim_type() {
        return eim_type;
    }

    public void setEim_type(String eim_type) {
        this.eim_type = eim_type;
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

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public String getOm_source() {
        return om_source;
    }

    public void setOm_source(String om_source) {
        this.om_source = om_source;
    }

    public int getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(int om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
    }

    public String getEim_id() {
        return eim_id;
    }

    public void setEim_id(String eim_id) {
        this.eim_id = eim_id;
    }

    public String getEim_version() {
        return eim_version;
    }

    public void setEim_version(String eim_version) {
        this.eim_version = eim_version;
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

    public String getEii_unit_num() {
        return eii_unit_num;
    }

    public void setEii_unit_num(String eii_unit_num) {
        this.eii_unit_num = eii_unit_num;
    }

    public String getEim_source() {
        return eim_source;
    }

    public void setEim_source(String eim_source) {
        this.eim_source = eim_source;
    }

    public String getEim_source_code() {
        return eim_source_code;
    }

    public void setEim_source_code(String eim_source_code) {
        this.eim_source_code = eim_source_code;
    }

    public String getSfkAmount() {
        return sfkAmount;
    }

    public void setSfkAmount(String sfkAmount) {
        this.sfkAmount = sfkAmount;
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

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getEii_unit_max() {
        return eii_unit_max;
    }

    public void setEii_unit_max(String eii_unit_max) {
        this.eii_unit_max = eii_unit_max;
    }

    public List<OutboundDetailBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OutboundDetailBean> dataList) {
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

    public String getEii_unit_min() {
        return eii_unit_min;
    }

    public void setEii_unit_min(String eii_unit_min) {
        this.eii_unit_min = eii_unit_min;
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
