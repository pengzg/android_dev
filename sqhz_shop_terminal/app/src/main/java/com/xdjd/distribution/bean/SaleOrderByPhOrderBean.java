package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SaleOrderByPhOrderBean extends BaseBean implements Serializable {
    private List<SaleOrderByPhOrderBean> listData;//	列表
    private String orderCode;//	订单编号
    private String addTime;//	下单时间
    private String totalAmount;//	金额
    private String salesName;

    private List<SaleOrderByPhOrderBean> listGoodsData;//	商品列表

    private String goodsName;//	商品名称
    private String phSaleNum_desc;//	销售数量
    private String refundNum_desc;//	撤货数量
    private String maxName;//	大单位名
    private String minName;//	小单位名
    private String minPrice;//	小单位价格
    private String maxPrice;//	大单位价格
    private String unitNum;//换算关系


    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getRefundNum_desc() {
        return refundNum_desc;
    }

    public void setRefundNum_desc(String refundNum_desc) {
        this.refundNum_desc = refundNum_desc;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public List<SaleOrderByPhOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<SaleOrderByPhOrderBean> listData) {
        this.listData = listData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleOrderByPhOrderBean> getListGoodsData() {
        return listGoodsData;
    }

    public void setListGoodsData(List<SaleOrderByPhOrderBean> listGoodsData) {
        this.listGoodsData = listGoodsData;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPhSaleNum_desc() {
        return phSaleNum_desc;
    }

    public void setPhSaleNum_desc(String phSaleNum_desc) {
        this.phSaleNum_desc = phSaleNum_desc;
    }

    public String getMaxName() {
        return maxName;
    }

    public void setMaxName(String maxName) {
        this.maxName = maxName;
    }

    public String getMinName() {
        return minName;
    }

    public void setMinName(String minName) {
        this.minName = minName;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
}
