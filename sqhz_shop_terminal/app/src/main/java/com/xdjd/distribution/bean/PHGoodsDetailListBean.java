package com.xdjd.distribution.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/12/19.
 */

public class PHGoodsDetailListBean extends BaseBean {
    private List<PHGoodsDetailListBean> listData = new ArrayList<>();
    private String goodsId;
    private String goodsName;//商品名称
    private String phTotalNum;//已铺货总数量
    private String phTotalNum_desc;
    private String phSaleNum;//已销售
    private String phSaleNum_desc;
    private String residueNum;//剩余
    private String residueNum_desc;
    private String refundNum_desc;//已撤货

    private String phShopNum = "";//铺货店铺数量

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getPhShopNum() {
        return phShopNum;
    }

    public void setPhShopNum(String phShopNum) {
        this.phShopNum = phShopNum;
    }

    public String getRefundNum_desc() {
        return refundNum_desc;
    }

    public void setRefundNum_desc(String refundNum_desc) {
        this.refundNum_desc = refundNum_desc;
    }

    public List<PHGoodsDetailListBean> getListData() {
        return listData;
    }

    public void setListData(List<PHGoodsDetailListBean> listData) {
        this.listData = listData;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
}
