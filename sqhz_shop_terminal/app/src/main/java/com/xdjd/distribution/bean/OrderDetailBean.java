package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderDetailBean extends BaseBean implements Serializable{

    private List<OrderDetailBean> listData;//	订单商品列表

    private String sign_url_nameref;//签名图片地址

    private String od_id;//	订单明细id
    private String od_goods_name;//	商品名称
    private String od_createdate;//	生产日期
    private String od_goods_num_min;//	小单位数量
    private String od_goods_num_max;//	大单位数量
    private String od_goods_unitname_min;//	小单位描述
    private String od_goods_unitname_max;//	大单位描述
    private String gg_model = "";//	商品规格
    private String od_price_min;//	商品小单位价格
    private String od_price_max;//	商品大单位价格
    private String od_real_total;//	商品总价格

    private String od_unit_num = "";//	单位换算

    private String gg_international_code_max;// 大单位
    private String gg_international_code_min = "";//小单位

    private String om_delivery_amount = "0";//	发货金额
    private String om_sett_amount = "0";//	收款金额

    private String is_cancel = "";//		Y可以取消 N不可以

    private int om_stats;//	订单状态 6是已发货收款完成
    private String om_version;

    private String om_order_amount;//	订单总金额

    private String od_goods_state;//	商品状态	商品状态 1 正 2 临 3残 4过
    private String od_goods_state_nameref;//	商品状态

    private String om_customerid_nameref;// 客户名称
    private String om_ordercode;//	订单编号
    private String om_stats_nameref;//	订单状态
    private String om_customer_name;
    private String om_mobile;


    // 现金
    private String xjAmount = "0.00";
    // 应收款（欠款）
    private String yskAmount= "0.00";
    // 优惠
    private String yhAmount= "0.00";
    // 刷卡
    private String skAmount= "0.00";
    // 实付款
    private String sfkAmount= "0.00";
    // 预收款
    private String yuskAmount= "0.00";

    //---------订单复制参数--------------
    private String om_ischages;//	是否变价
    private String om_storeid;//	出库仓库
    private String om_storeId_name;//	出库仓库名称
    private int om_ordertype;//	订单类型 1 普通 2 处理 3 退货 4 换货 5 还货 6铺货销售
    private String om_source = "";//	订单来源	订单来源 1 客户商城下单;2 终端申请;3 车销;4 库管下单;5 后台下单--2，3能复制


    public String getSign_url_nameref() {
        return sign_url_nameref;
    }

    public void setSign_url_nameref(String sign_url_nameref) {
        this.sign_url_nameref = sign_url_nameref;
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

    public String getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(String yhAmount) {
        this.yhAmount = yhAmount;
    }

    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public String getSfkAmount() {
        return sfkAmount;
    }

    public void setSfkAmount(String sfkAmount) {
        this.sfkAmount = sfkAmount;
    }

    public String getYuskAmount() {
        return yuskAmount;
    }

    public void setYuskAmount(String yuskAmount) {
        this.yuskAmount = yuskAmount;
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

    public String getOm_version() {
        return om_version;
    }

    public void setOm_version(String om_version) {
        this.om_version = om_version;
    }

    public String getOm_customerid_nameref() {
        return om_customerid_nameref;
    }

    public void setOm_customerid_nameref(String om_customerid_nameref) {
        this.om_customerid_nameref = om_customerid_nameref;
    }

    public String getOm_ordercode() {
        return om_ordercode;
    }

    public void setOm_ordercode(String om_ordercode) {
        this.om_ordercode = om_ordercode;
    }

    public String getOm_stats_nameref() {
        return om_stats_nameref;
    }

    public void setOm_stats_nameref(String om_stats_nameref) {
        this.om_stats_nameref = om_stats_nameref;
    }

    public String getOd_goods_state_nameref() {
        return od_goods_state_nameref;
    }

    public void setOd_goods_state_nameref(String od_goods_state_nameref) {
        this.od_goods_state_nameref = od_goods_state_nameref;
    }

    public String getOd_goods_state() {
        return od_goods_state;
    }

    public void setOd_goods_state(String od_goods_state) {
        this.od_goods_state = od_goods_state;
    }

    public String getOm_order_amount() {
        return om_order_amount;
    }

    public void setOm_order_amount(String om_order_amount) {
        this.om_order_amount = om_order_amount;
    }

    public int getOm_stats() {
        return om_stats;
    }

    public void setOm_stats(int om_stats) {
        this.om_stats = om_stats;
    }

    public String getOm_source() {
        return om_source;
    }

    public void setOm_source(String om_source) {
        this.om_source = om_source;
    }

    public String getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(String is_cancel) {
        this.is_cancel = is_cancel;
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

    public String getOm_ischages() {
        return om_ischages;
    }

    public void setOm_ischages(String om_ischages) {
        this.om_ischages = om_ischages;
    }

    public String getOm_storeid() {
        return om_storeid;
    }

    public void setOm_storeid(String om_storeid) {
        this.om_storeid = om_storeid;
    }

    public String getOm_storeId_name() {
        return om_storeId_name;
    }

    public void setOm_storeId_name(String om_storeId_name) {
        this.om_storeId_name = om_storeId_name;
    }

    public int getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(int om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getOd_real_total() {
        return od_real_total;
    }

    public void setOd_real_total(String od_real_total) {
        this.od_real_total = od_real_total;
    }

    public List<OrderDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<OrderDetailBean> listData) {
        this.listData = listData;
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

    public String getOd_createdate() {
        return od_createdate;
    }

    public void setOd_createdate(String od_createdate) {
        this.od_createdate = od_createdate;
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
}
