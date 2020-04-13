package com.xdjd.storebox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PHOrderDetailBean extends BaseBean implements Cloneable,Serializable{

    private List<PHOrderDetailBean> listData;//	铺货列表

    private String apply_order_code;//	铺货单号
    private List<PHOrderDetailBean> list;//铺货单、销售、撤货

    private String orderCode;//	订单编号
    private String totalAmount;//	总金额
    private String addTime;//	下单时间
    private List<PHOrderDetailBean> listGoodsData;//	订单详情列表

    private String oad_id;//	明细id
    private String oad_goods_price_id;//	Priceid
    private String goodsName;//	商品名
    private String maxPrice;//	大单位价格
    private String minPrice;//	小单位价格
    private String maxName;//	大单位名
    private String minName;//	小单位名
    private String syGoodsAmount;//	剩余商品金额
    private String phTotalNum;//	铺货总数量
    private String phSaleNum;//	已销售数量
    private String residueNum;//	剩余数量
    private String phTotalNum_desc;
    private String phSaleNum_desc;
    private String residueNum_desc;
    private String unitNum;//	换算关系

    private String oad_goods_id;//	商品id
    private String oad_apply_id;//	申请单id
    private String refundNum;// 退货数量
    private String refundNum_desc;

    private String goodsCode;//商品条码

    private String residueNum_min;
    private String residueNum_max;

    private String saleAmount;// 销售金额
    private String refundAmount;// 退货金额



    private String yhAmount = "";//优惠
    private String xjAmount="";//现金
    private String yeAmount="";//余额
    private String ysAmount="";//欠款
    private String skAmount="";//刷卡

    //----------自定义参数---------
    private String totalPrice = "0"; //总价格

    private String maxNum = "0";//商品最大数量
    private String minNum = "0";//商品最小数量

    private String newMaxPrice;//	新的大单位价格
    private String newMinPrice;//	新的小单位价格

    public String getYhAmount() {
        return yhAmount;
    }

    public void setYhAmount(String yhAmount) {
        this.yhAmount = yhAmount;
    }

    public String getXjAmount() {
        return xjAmount;
    }

    public void setXjAmount(String xjAmount) {
        this.xjAmount = xjAmount;
    }

    public String getYeAmount() {
        return yeAmount;
    }

    public void setYeAmount(String yeAmount) {
        this.yeAmount = yeAmount;
    }

    public String getYsAmount() {
        return ysAmount;
    }

    public void setYsAmount(String ysAmount) {
        this.ysAmount = ysAmount;
    }

    public String getSkAmount() {
        return skAmount;
    }

    public void setSkAmount(String skAmount) {
        this.skAmount = skAmount;
    }

    public List<PHOrderDetailBean> getList() {
        return list;
    }

    public void setList(List<PHOrderDetailBean> list) {
        this.list = list;
    }

    public String getApply_order_code() {
        return apply_order_code;
    }

    public void setApply_order_code(String apply_order_code) {
        this.apply_order_code = apply_order_code;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
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

    public String getRefundNum_desc() {
        return refundNum_desc;
    }

    public void setRefundNum_desc(String refundNum_desc) {
        this.refundNum_desc = refundNum_desc;
    }

    public String getOad_goods_id() {
        return oad_goods_id;
    }

    public void setOad_goods_id(String oad_goods_id) {
        this.oad_goods_id = oad_goods_id;
    }

    public String getOad_apply_id() {
        return oad_apply_id;
    }

    public void setOad_apply_id(String oad_apply_id) {
        this.oad_apply_id = oad_apply_id;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public String getNewMaxPrice() {
        return newMaxPrice;
    }

    public void setNewMaxPrice(String newMaxPrice) {
        this.newMaxPrice = newMaxPrice;
    }

    public String getNewMinPrice() {
        return newMinPrice;
    }

    public void setNewMinPrice(String newMinPrice) {
        this.newMinPrice = newMinPrice;
    }

    @Override
    public Object clone() {
        PHOrderDetailBean bean = null;
        try{
            bean = (PHOrderDetailBean)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return bean;
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

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOad_id() {
        return oad_id;
    }

    public void setOad_id(String oad_id) {
        this.oad_id = oad_id;
    }

    public String getOad_goods_price_id() {
        return oad_goods_price_id;
    }

    public void setOad_goods_price_id(String oad_goods_price_id) {
        this.oad_goods_price_id = oad_goods_price_id;
    }

    public String getResidueNum_min() {
        return residueNum_min;
    }

    public void setResidueNum_min(String residueNum_min) {
        this.residueNum_min = residueNum_min;
    }

    public String getResidueNum_max() {
        return residueNum_max;
    }

    public void setResidueNum_max(String residueNum_max) {
        this.residueNum_max = residueNum_max;
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

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public List<PHOrderDetailBean> getListData() {
        return listData;
    }

    public void setListData(List<PHOrderDetailBean> listData) {
        this.listData = listData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public List<PHOrderDetailBean> getListGoodsData() {
        return listGoodsData;
    }

    public void setListGoodsData(List<PHOrderDetailBean> listGoodsData) {
        this.listGoodsData = listGoodsData;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public String getSyGoodsAmount() {
        return syGoodsAmount;
    }

    public void setSyGoodsAmount(String syGoodsAmount) {
        this.syGoodsAmount = syGoodsAmount;
    }

    public String getPhTotalNum() {
        return phTotalNum;
    }

    public void setPhTotalNum(String phTotalNum) {
        this.phTotalNum = phTotalNum;
    }

    public String getPhSaleNum() {
        return phSaleNum;
    }

    public void setPhSaleNum(String phSaleNum) {
        this.phSaleNum = phSaleNum;
    }

    public String getResidueNum() {
        return residueNum;
    }

    public void setResidueNum(String residueNum) {
        this.residueNum = residueNum;
    }
}
