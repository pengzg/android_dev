package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ShopAuditBean extends BaseBean {

    private List<ShopAuditBean> winningList;//	列表

    private String productname;//	产品名
    private String shopid;//	店铺id
    private String shopname;//	店铺名
    private String wjsnum;//	未结算
    private String yjsnum;//	已结算
    private String zsnum;//	总数

    private String unit;//	单位
    private String mw_prizeid;//	奖品id

    public String getMw_prizeid() {
        return mw_prizeid;
    }

    public void setMw_prizeid(String mw_prizeid) {
        this.mw_prizeid = mw_prizeid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ShopAuditBean> getWinningList() {
        return winningList;
    }

    public void setWinningList(List<ShopAuditBean> winningList) {
        this.winningList = winningList;
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

    public String getWjsnum() {
        return wjsnum;
    }

    public void setWjsnum(String wjsnum) {
        this.wjsnum = wjsnum;
    }

    public String getYjsnum() {
        return yjsnum;
    }

    public void setYjsnum(String yjsnum) {
        this.yjsnum = yjsnum;
    }

    public String getZsnum() {
        return zsnum;
    }

    public void setZsnum(String zsnum) {
        this.zsnum = zsnum;
    }
}
