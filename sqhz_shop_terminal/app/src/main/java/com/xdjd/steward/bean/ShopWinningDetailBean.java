package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ShopWinningDetailBean extends BaseBean {
    private String num;//	总数
    private String unit;//	单位
    private List<ShopWinningDetailBean> hxList;//

    private String shopid;//	店铺id
    private String shopname;//	店铺名称
    private String mw_prizename;//	奖品名称
    private String productname;//	商品名称
    private String total_nums;//	总数量
    private String ydnum;//	已兑人数
    private String wdnum;//	未兑人数
    private String zsnum;//	总商品数量
    private String yjsnum;//	已结算商品数
    private String wjsnum;//	未结算商品数
    private String mw_prizeid;//	奖品id

    public String getMw_prizeid() {
        return mw_prizeid;
    }

    public void setMw_prizeid(String mw_prizeid) {
        this.mw_prizeid = mw_prizeid;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ShopWinningDetailBean> getHxList() {
        return hxList;
    }

    public void setHxList(List<ShopWinningDetailBean> hxList) {
        this.hxList = hxList;
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

    public String getMw_prizename() {
        return mw_prizename;
    }

    public void setMw_prizename(String mw_prizename) {
        this.mw_prizename = mw_prizename;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public String getYdnum() {
        return ydnum;
    }

    public void setYdnum(String ydnum) {
        this.ydnum = ydnum;
    }

    public String getWdnum() {
        return wdnum;
    }

    public void setWdnum(String wdnum) {
        this.wdnum = wdnum;
    }

    public String getZsnum() {
        return zsnum;
    }

    public void setZsnum(String zsnum) {
        this.zsnum = zsnum;
    }

    public String getYjsnum() {
        return yjsnum;
    }

    public void setYjsnum(String yjsnum) {
        this.yjsnum = yjsnum;
    }

    public String getWjsnum() {
        return wjsnum;
    }

    public void setWjsnum(String wjsnum) {
        this.wjsnum = wjsnum;
    }
}
