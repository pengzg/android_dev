package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/15
 *     desc   : 选中商品的bean
 *     version: 1.0
 * </pre>
 */

public class GoodsBuyBean {

    private GoodsBean mGoodsBean;//商品原有信息


    private String maxNum;//商品最大数量
    private String minNum;//商品最小数量

    private String maxPrice;//	最大单位价格
    private String minPrice;//	最小单位价格

    private String totalPrice; //总价格

    private String stock_nameref;//库存描述
    private String remarks; //备注信息

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStock_nameref() {
        return stock_nameref;
    }

    public void setStock_nameref(String stock_nameref) {
        this.stock_nameref = stock_nameref;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public GoodsBean getGoodsBean() {
        return mGoodsBean;
    }

    public void setGoodsBean(GoodsBean goodsBean) {
        mGoodsBean = goodsBean;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }

    public String getMinNum() {
        return minNum;
    }

    public void setMinNum(String minNum) {
        this.minNum = minNum;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
}
