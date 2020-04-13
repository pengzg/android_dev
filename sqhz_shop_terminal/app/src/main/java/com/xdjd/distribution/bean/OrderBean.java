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

public class OrderBean extends BaseBean implements Serializable{

    private List<OrderBean> listData;//	订单商品列表

    //如果是活动的订单,不让修改商品数量
    private String flag;//	是否允许修改数量 1 允许 2 不允许

    private String om_id;//	订单id
    private String om_customerid_nameref = "";//	客户名称
    private String om_ordertype_nameref = "";//	订单类型
    private String om_order_amount;//	订单金额
    private String om_orderdate;//	订单日期
    private String om_clerkid;//	配送员id
    private String om_clerkid_nameref = "";//	配送员姓名
    private String om_version;//	版本号
    private String om_ordercode = ""; //订单编号
    private String gg_model;//
    private String om_address;//客户地址

    private String om_salesid_nameref = "";// 订单中的业务员

    private String om_storeid_nameref = "";//	仓库

    private String contact_name = "";
    private String contact_mobile = "";

    private String om_stats_nameref = "";//订单状态

    private String om_delivery_amount = "0";//	发货金额
    private String om_sett_amount = "0";//	收款金额

    private int om_stats;//	订单状态值 om_stats<6
    private String om_salesid;//	登录人id
    private String om_source;//  当om_salesid=用户id && om_stats<6 && om_source=2或者3时;显示取消订单按钮

    //配送任务列表
    private List<DistributionGoodsBean> orderDetailVoList;//	订单明细list	只有from为2时

    private List<OrderBean> orderList1;//	普通
    private List<OrderBean> orderList2;//	处理
    private List<OrderBean> orderList3;//	退货
    private List<OrderBean> orderList4;//	换货
    private List<OrderBean> orderList5;//	还货


    public String getOm_address() {
        return om_address;
    }

    public void setOm_address(String om_address) {
        this.om_address = om_address;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<OrderBean> getOrderList5() {
        return orderList5;
    }

    public void setOrderList5(List<OrderBean> orderList5) {
        this.orderList5 = orderList5;
    }

    public String getOm_delivery_amount() {
        return om_delivery_amount;
    }

    public void setOm_delivery_amount(String om_delivery_amount) {
        this.om_delivery_amount = om_delivery_amount;
    }

    public String getOm_sett_amount() {
        return om_sett_amount;
    }

    public void setOm_sett_amount(String om_sett_amount) {
        this.om_sett_amount = om_sett_amount;
    }

    public String getOm_storeid_nameref() {
        return om_storeid_nameref;
    }

    public void setOm_storeid_nameref(String om_storeid_nameref) {
        this.om_storeid_nameref = om_storeid_nameref;
    }

    public String getOm_salesid_nameref() {
        return om_salesid_nameref;
    }

    public void setOm_salesid_nameref(String om_salesid_nameref) {
        this.om_salesid_nameref = om_salesid_nameref;
    }

    public int getOm_stats() {
        return om_stats;
    }

    public void setOm_stats(int om_stats) {
        this.om_stats = om_stats;
    }

    public String getOm_salesid() {
        return om_salesid;
    }

    public void setOm_salesid(String om_salesid) {
        this.om_salesid = om_salesid;
    }

    public String getOm_source() {
        return om_source;
    }

    public void setOm_source(String om_source) {
        this.om_source = om_source;
    }

    public String getOm_stats_nameref() {
        return om_stats_nameref;
    }

    public void setOm_stats_nameref(String om_stats_nameref) {
        this.om_stats_nameref = om_stats_nameref;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getOm_ordercode() {
        return om_ordercode;
    }

    public void setOm_ordercode(String om_ordercode) {
        this.om_ordercode = om_ordercode;
    }

    public String getOm_version() {
        return om_version;
    }

    public void setOm_version(String om_version) {
        this.om_version = om_version;
    }

    public List<OrderBean> getOrderList1() {
        return orderList1;
    }

    public void setOrderList1(List<OrderBean> orderList1) {
        this.orderList1 = orderList1;
    }

    public List<OrderBean> getOrderList2() {
        return orderList2;
    }

    public void setOrderList2(List<OrderBean> orderList2) {
        this.orderList2 = orderList2;
    }

    public List<OrderBean> getOrderList3() {
        return orderList3;
    }

    public void setOrderList3(List<OrderBean> orderList3) {
        this.orderList3 = orderList3;
    }

    public List<OrderBean> getOrderList4() {
        return orderList4;
    }

    public void setOrderList4(List<OrderBean> orderList4) {
        this.orderList4 = orderList4;
    }

    public String getOm_clerkid() {
        return om_clerkid;
    }

    public void setOm_clerkid(String om_clerkid) {
        this.om_clerkid = om_clerkid;
    }

    public String getOm_clerkid_nameref() {
        return om_clerkid_nameref;
    }

    public void setOm_clerkid_nameref(String om_clerkid_nameref) {
        this.om_clerkid_nameref = om_clerkid_nameref;
    }

    public List<DistributionGoodsBean> getOrderDetailVoList() {
        return orderDetailVoList;
    }

    public void setOrderDetailVoList(List<DistributionGoodsBean> orderDetailVoList) {
        this.orderDetailVoList = orderDetailVoList;
    }

    public List<OrderBean> getListData() {
        return listData;
    }

    public void setListData(List<OrderBean> listData) {
        this.listData = listData;
    }

    public String getOm_id() {
        return om_id;
    }

    public void setOm_id(String om_id) {
        this.om_id = om_id;
    }

    public String getOm_customerid_nameref() {
        return om_customerid_nameref;
    }

    public void setOm_customerid_nameref(String om_customerid_nameref) {
        this.om_customerid_nameref = om_customerid_nameref;
    }

    public String getOm_ordertype_nameref() {
        return om_ordertype_nameref;
    }

    public void setOm_ordertype_nameref(String om_ordertype_nameref) {
        this.om_ordertype_nameref = om_ordertype_nameref;
    }

    public String getOm_order_amount() {
        return om_order_amount;
    }

    public void setOm_order_amount(String om_order_amount) {
        this.om_order_amount = om_order_amount;
    }

    public String getOm_orderdate() {
        return om_orderdate;
    }

    public void setOm_orderdate(String om_orderdate) {
        this.om_orderdate = om_orderdate;
    }
}
