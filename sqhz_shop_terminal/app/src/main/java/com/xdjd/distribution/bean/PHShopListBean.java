package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PHShopListBean extends BaseBean {
    private List<PHShopListBean> listData;
    private String shopName;//	店铺名称
    private String goodsKindNum;//	已铺货商品种类数
    private String shopId;//	店铺id
    private String firstPhDate;//	首次铺货时间

    public List<PHShopListBean> getListData() {
        return listData;
    }

    public void setListData(List<PHShopListBean> listData) {
        this.listData = listData;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getGoodsKindNum() {
        return goodsKindNum;
    }

    public void setGoodsKindNum(String goodsKindNum) {
        this.goodsKindNum = goodsKindNum;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getFirstPhDate() {
        return firstPhDate;
    }

    public void setFirstPhDate(String firstPhDate) {
        this.firstPhDate = firstPhDate;
    }
}
