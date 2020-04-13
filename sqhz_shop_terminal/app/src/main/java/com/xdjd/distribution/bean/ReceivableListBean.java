package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/28
 *     desc   : 客户应收款bean
 *     version: 1.0
 * </pre>
 */

public class ReceivableListBean extends BaseBean {

    private String totalAmount = "0.00";//	总金额
    private String totalTradeAmount = "0.00";//	交易总金额
    private String totalDiscountAmount = "0.00";//	优惠总金额
    private String totalWsAmount = "0.00";//	未收总金额
    private int total;//总条数

    private List<ReceivableListBean> listData;//	商品列表

    private String gr_customerid;//客户id
    private String gr_customerid_nameref;//	客户名
    private String gr_total_amount;//	总金额
    private String gr_trade_amount;//	实际交易金额
    private String ws_amount = "0";//	未收金额
    private String gr_id;//
    private String gr_sourcecode;//	来源编码
    private String gr_sourceid;//	来源id

    private String gr_version;//	版本号
    private String gr_discount_amount;//	优惠金额

    private int isSelect = 0;//0是选中,1是未选中
    private String et_ws_amount = "0";//编辑框中的未收金额
    private String gr_this_amount;//	金额


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalTradeAmount() {
        return totalTradeAmount;
    }

    public void setTotalTradeAmount(String totalTradeAmount) {
        this.totalTradeAmount = totalTradeAmount;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getTotalWsAmount() {
        return totalWsAmount;
    }

    public void setTotalWsAmount(String totalWsAmount) {
        this.totalWsAmount = totalWsAmount;
    }

    public String getGr_this_amount() {
        return gr_this_amount;
    }

    public void setGr_this_amount(String gr_this_amount) {
        this.gr_this_amount = gr_this_amount;
    }

    public String getGr_version() {
        return gr_version;
    }

    public void setGr_version(String gr_version) {
        this.gr_version = gr_version;
    }

    public String getEt_ws_amount() {
        return et_ws_amount;
    }

    public void setEt_ws_amount(String et_ws_amount) {
        this.et_ws_amount = et_ws_amount;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getGr_customerid() {
        return gr_customerid;
    }

    public void setGr_customerid(String gr_customerid) {
        this.gr_customerid = gr_customerid;
    }

    public String getGr_discount_amount() {
        return gr_discount_amount;
    }

    public void setGr_discount_amount(String gr_discount_amount) {
        this.gr_discount_amount = gr_discount_amount;
    }

    public String getGr_sourcecode() {
        return gr_sourcecode;
    }

    public void setGr_sourcecode(String gr_sourcecode) {
        this.gr_sourcecode = gr_sourcecode;
    }

    public String getGr_sourceid() {
        return gr_sourceid;
    }

    public void setGr_sourceid(String gr_sourceid) {
        this.gr_sourceid = gr_sourceid;
    }

    public String getGr_id() {
        return gr_id;
    }

    public void setGr_id(String gr_id) {
        this.gr_id = gr_id;
    }

    public List<ReceivableListBean> getListData() {
        return listData;
    }

    public void setListData(List<ReceivableListBean> listData) {
        this.listData = listData;
    }

    public String getGr_customerid_nameref() {
        return gr_customerid_nameref;
    }

    public void setGr_customerid_nameref(String gr_customerid_nameref) {
        this.gr_customerid_nameref = gr_customerid_nameref;
    }

    public String getGr_total_amount() {
        return gr_total_amount;
    }

    public void setGr_total_amount(String gr_total_amount) {
        this.gr_total_amount = gr_total_amount;
    }

    public String getGr_trade_amount() {
        return gr_trade_amount;
    }

    public void setGr_trade_amount(String gr_trade_amount) {
        this.gr_trade_amount = gr_trade_amount;
    }

    public String getWs_amount() {
        return ws_amount;
    }

    public void setWs_amount(String ws_amount) {
        this.ws_amount = ws_amount;
    }
}
