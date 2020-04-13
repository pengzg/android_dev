package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/11
 *     desc   : 核销兑奖记录bean
 *     version: 1.0
 * </pre>
 */

public class WinningListBean extends BaseBean {

    private List<WinningListBean> winningList;//	列表

    private String productname;//	产品名
    private String shopid;//	店铺id
    private String shopname;//	核销店铺名
    private String mw_memberid;//	会员 id
    private String mw_memberid_nameref;//	会员名
    private String mw_receivetime;//	领取时间
    private String unit;//	单位
    private String mw_num;//	数量


    private List<WinningListBean> hxList;//小店结算信息列表
    private String num;//结算商品总数量

    private String mw_check_state;//	结算状态
    private String mw_check_state_nameref;//	结算状态
    private String mw_id;//	数据 id

    private String mw_shopid_nameref;//	中奖店铺
    private String mw_winningtime;//	中奖时间

    private String mw_memberid_image;//会员头像


    public String getMw_memberid_image() {
        return mw_memberid_image;
    }

    public void setMw_memberid_image(String mw_memberid_image) {
        this.mw_memberid_image = mw_memberid_image;
    }

    public String getMw_shopid_nameref() {
        return mw_shopid_nameref;
    }

    public void setMw_shopid_nameref(String mw_shopid_nameref) {
        this.mw_shopid_nameref = mw_shopid_nameref;
    }

    public String getMw_winningtime() {
        return mw_winningtime;
    }

    public void setMw_winningtime(String mw_winningtime) {
        this.mw_winningtime = mw_winningtime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<WinningListBean> getHxList() {
        return hxList;
    }

    public void setHxList(List<WinningListBean> hxList) {
        this.hxList = hxList;
    }

    public List<WinningListBean> getWinningList() {
        return winningList;
    }

    public void setWinningList(List<WinningListBean> winningList) {
        this.winningList = winningList;
    }

    public String getMw_check_state() {
        return mw_check_state;
    }

    public void setMw_check_state(String mw_check_state) {
        this.mw_check_state = mw_check_state;
    }

    public String getMw_check_state_nameref() {
        return mw_check_state_nameref;
    }

    public void setMw_check_state_nameref(String mw_check_state_nameref) {
        this.mw_check_state_nameref = mw_check_state_nameref;
    }

    public String getMw_id() {
        return mw_id;
    }

    public void setMw_id(String mw_id) {
        this.mw_id = mw_id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getMw_memberid() {
        return mw_memberid;
    }

    public void setMw_memberid(String mw_memberid) {
        this.mw_memberid = mw_memberid;
    }

    public String getMw_memberid_nameref() {
        return mw_memberid_nameref;
    }

    public void setMw_memberid_nameref(String mw_memberid_nameref) {
        this.mw_memberid_nameref = mw_memberid_nameref;
    }

    public String getMw_receivetime() {
        return mw_receivetime;
    }

    public void setMw_receivetime(String mw_receivetime) {
        this.mw_receivetime = mw_receivetime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMw_num() {
        return mw_num;
    }

    public void setMw_num(String mw_num) {
        this.mw_num = mw_num;
    }
}
