package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/8
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PhShopHzBean extends BaseBean implements Serializable {

    private List<PhShopHzBean> listData;//	列表

    private String shopId;//店铺id
    private String shopName;//	店铺名称
    private String lastPhDate;//	最近铺货时间
    private String goodsName;//	商品名
    private String phTotalNum_desc;//	铺货总数量描述
    private String phSaleNum_desc;//	已销售数量描述
    private String residueNum_desc;//	未销售数量描述
    private String refundNum_desc;//	已撤货数量描述

    private String goodsKindNum = "0";//	商品种类数
    private String totalAmount;//	总金额
    private String refundAmount;//	已撤货
    private String saleAmount;//	已销售
    private String syGoodsAmount;//	未销售


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<PhShopHzBean> getListData() {
        return listData;
    }

    public void setListData(List<PhShopHzBean> listData) {
        this.listData = listData;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLastPhDate() {
        return lastPhDate;
    }

    public void setLastPhDate(String lastPhDate) {
        this.lastPhDate = lastPhDate;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPhTotalNum_desc() {
        return phTotalNum_desc;
    }

    public void setPhTotalNum_desc(String phTotalNum_desc) {
        this.phTotalNum_desc = phTotalNum_desc;
    }

    public String getPhSaleNum_desc() {
        return phSaleNum_desc;
    }

    public void setPhSaleNum_desc(String phSaleNum_desc) {
        this.phSaleNum_desc = phSaleNum_desc;
    }

    public String getResidueNum_desc() {
        return residueNum_desc;
    }

    public void setResidueNum_desc(String residueNum_desc) {
        this.residueNum_desc = residueNum_desc;
    }

    public String getRefundNum_desc() {
        return refundNum_desc;
    }

    public void setRefundNum_desc(String refundNum_desc) {
        this.refundNum_desc = refundNum_desc;
    }

    public String getGoodsKindNum() {
        return goodsKindNum;
    }

    public void setGoodsKindNum(String goodsKindNum) {
        this.goodsKindNum = goodsKindNum;
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
