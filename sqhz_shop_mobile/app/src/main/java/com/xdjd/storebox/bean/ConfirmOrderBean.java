package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 确认结算单bean
 * Created by lijipei on 2016/12/5.
 */

public class ConfirmOrderBean extends  BaseBean{

    private String goodsNum;//	商品数量
    private String amount;//	订单应付金额
    private String goodsAmount;//	商品金额
    private String discountAmount;//	优惠金额

    private String shopName;//	店铺名称
    private String receiverName = "";//	收货人
    private String mobile = "";//	收货人手机号
    private String address;//	送货地址
    private String addressId;//地址id

    private String freightAmount;//	运费

    private String jiajiagouName = ""; //加价购活动字段
    private JiajiagouGoodsBean jiajiagouGoods;//	加价购商品信息

    private String payMethodDesc;//选择支付方式,优惠提示字段

    private List<OrderGoodsDetailBean> dataList;//购买的商品详情列表
    private List<PayMethods> payMethods; //支付方式


    public class JiajiagouGoodsBean{
        private String goodsAmount;//	商品价格
        private String goodsName;//	商品名称
        private String goodsCover;//	商品图片

        public String getGoodsAmount() {
            return goodsAmount;
        }

        public void setGoodsAmount(String goodsAmount) {
            this.goodsAmount = goodsAmount;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsCover() {
            return goodsCover;
        }

        public void setGoodsCover(String goodsCover) {
            this.goodsCover = goodsCover;
        }
    }

    public String getPayMethodDesc() {
        return payMethodDesc;
    }

    public void setPayMethodDesc(String payMethodDesc) {
        this.payMethodDesc = payMethodDesc;
    }

    public JiajiagouGoodsBean getJiajiagouGoods() {
        return jiajiagouGoods;
    }

    public void setJiajiagouGoods(JiajiagouGoodsBean jiajiagouGoods) {
        this.jiajiagouGoods = jiajiagouGoods;
    }

    public String getJiajiagouName() {
        return jiajiagouName;
    }

    public void setJiajiagouName(String jiajiagouName) {
        this.jiajiagouName = jiajiagouName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<OrderGoodsDetailBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderGoodsDetailBean> dataList) {
        this.dataList = dataList;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(String goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public List<PayMethods> getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(List<PayMethods> payMethods) {
        this.payMethods = payMethods;
    }


}
