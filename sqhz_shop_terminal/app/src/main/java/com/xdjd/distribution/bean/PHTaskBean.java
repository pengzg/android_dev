package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PHTaskBean extends BaseBean implements Serializable{

    private List<PHTaskBean> orderList;//	普通

    private String oa_id;//	订单id
    private String oa_applycode;//订单号
    private String oa_customerid_nameref;//	客户名称
    private String oa_ordertype_nameref;//	订单类型
    private String oa_order_amount;//	订单金额
    private String oa_orderdate;//	订单日期
    private String oa_clerkid;//	配送员id
    private String oa_clerkid_nameref;//	配送员姓名
    private String oa_version;//	版本号
    private String oa_deliverydate;//	配送日期
    private String oa_mobile;//	电话
    private String oa_address;//	地址

    private List<PHTaskBean> detailList;//	订单明细list

    private String oad_id;//	订单明细id
    private String oad_goods_name;//	商品名称
    private String oad_createdate;//	生产日期
    private String oad_goods_num_min = "0";//	小单位数量
    private String oad_goods_num_max = "0";//	大单位数量
    private String oad_goods_unitname_min;//	小单位描述
    private String oad_goods_unitname_max;//	大单位描述
    private String gg_model;//	商品规格
    private String oad_price_min;//	商品小单位价格
    private String oad_price_max;//	商品大单位价格
    private String oad_real_total;//	商品总价格
    private String oad_goods_price_id;//	商品价格主键
    private String oad_unit_num;//	换算单位
    private String oad_goodstype;//	商品类型 1 普通 2 赠品
    private String oad_total_amount;

    public String getOad_total_amount() {
        return oad_total_amount;
    }

    public void setOad_total_amount(String oad_total_amount) {
        this.oad_total_amount = oad_total_amount;
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

    public String getOad_real_total() {
        return oad_real_total;
    }

    public void setOad_real_total(String oad_real_total) {
        this.oad_real_total = oad_real_total;
    }

    public String getOad_goods_price_id() {
        return oad_goods_price_id;
    }

    public void setOad_goods_price_id(String oad_goods_price_id) {
        this.oad_goods_price_id = oad_goods_price_id;
    }

    public String getOad_unit_num() {
        return oad_unit_num;
    }

    public void setOad_unit_num(String oad_unit_num) {
        this.oad_unit_num = oad_unit_num;
    }

    public String getOad_goodstype() {
        return oad_goodstype;
    }

    public void setOad_goodstype(String oad_goodstype) {
        this.oad_goodstype = oad_goodstype;
    }

    public String getOa_applycode() {
        return oa_applycode;
    }

    public void setOa_applycode(String oa_applycode) {
        this.oa_applycode = oa_applycode;
    }

    public List<PHTaskBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<PHTaskBean> orderList) {
        this.orderList = orderList;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getOa_customerid_nameref() {
        return oa_customerid_nameref;
    }

    public void setOa_customerid_nameref(String oa_customerid_nameref) {
        this.oa_customerid_nameref = oa_customerid_nameref;
    }

    public String getOa_ordertype_nameref() {
        return oa_ordertype_nameref;
    }

    public void setOa_ordertype_nameref(String oa_ordertype_nameref) {
        this.oa_ordertype_nameref = oa_ordertype_nameref;
    }

    public String getOa_order_amount() {
        return oa_order_amount;
    }

    public void setOa_order_amount(String oa_order_amount) {
        this.oa_order_amount = oa_order_amount;
    }

    public String getOa_orderdate() {
        return oa_orderdate;
    }

    public void setOa_orderdate(String oa_orderdate) {
        this.oa_orderdate = oa_orderdate;
    }

    public String getOa_clerkid() {
        return oa_clerkid;
    }

    public void setOa_clerkid(String oa_clerkid) {
        this.oa_clerkid = oa_clerkid;
    }

    public String getOa_clerkid_nameref() {
        return oa_clerkid_nameref;
    }

    public void setOa_clerkid_nameref(String oa_clerkid_nameref) {
        this.oa_clerkid_nameref = oa_clerkid_nameref;
    }

    public String getOa_version() {
        return oa_version;
    }

    public void setOa_version(String oa_version) {
        this.oa_version = oa_version;
    }

    public String getOa_deliverydate() {
        return oa_deliverydate;
    }

    public void setOa_deliverydate(String oa_deliverydate) {
        this.oa_deliverydate = oa_deliverydate;
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

    public List<PHTaskBean> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<PHTaskBean> detailList) {
        this.detailList = detailList;
    }

    public String getOad_id() {
        return oad_id;
    }

    public void setOad_id(String oad_id) {
        this.oad_id = oad_id;
    }

    public String getOad_goods_name() {
        return oad_goods_name;
    }

    public void setOad_goods_name(String oad_goods_name) {
        this.oad_goods_name = oad_goods_name;
    }

    public String getOad_createdate() {
        return oad_createdate;
    }

    public void setOad_createdate(String oad_createdate) {
        this.oad_createdate = oad_createdate;
    }

    public String getOad_goods_num_min() {
        return oad_goods_num_min;
    }

    public void setOad_goods_num_min(String oad_goods_num_min) {
        this.oad_goods_num_min = oad_goods_num_min;
    }

    public String getOad_goods_num_max() {
        return oad_goods_num_max;
    }

    public void setOad_goods_num_max(String oad_goods_num_max) {
        this.oad_goods_num_max = oad_goods_num_max;
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

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }
}
