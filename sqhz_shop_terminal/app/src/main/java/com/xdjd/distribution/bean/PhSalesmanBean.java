package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PhSalesmanBean extends BaseBean implements Serializable {

    private List<PhSalesmanBean> listData;//	列表

    private String salesid;
    private String salesid_nameref;//	业务员名称
    private String phShopNum;//	已铺货店铺数量
    private String chShopNum;//	已撤货店铺数量
    private String totalAmount;//	铺货金额
    private String refundAmount;//	撤货金额
    private String saleAmount;//	销售金额
    private String syGoodsAmount;//	未销售金额


    public String getSalesid() {
        return salesid;
    }

    public void setSalesid(String salesid) {
        this.salesid = salesid;
    }

    public List<PhSalesmanBean> getListData() {
        return listData;
    }

    public void setListData(List<PhSalesmanBean> listData) {
        this.listData = listData;
    }

    public String getSalesid_nameref() {
        return salesid_nameref;
    }

    public void setSalesid_nameref(String salesid_nameref) {
        this.salesid_nameref = salesid_nameref;
    }

    public String getPhShopNum() {
        return phShopNum;
    }

    public void setPhShopNum(String phShopNum) {
        this.phShopNum = phShopNum;
    }

    public String getChShopNum() {
        return chShopNum;
    }

    public void setChShopNum(String chShopNum) {
        this.chShopNum = chShopNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(String saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getSyGoodsAmount() {
        return syGoodsAmount;
    }

    public void setSyGoodsAmount(String syGoodsAmount) {
        this.syGoodsAmount = syGoodsAmount;
    }
}
