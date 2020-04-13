package com.xdjd.storebox.bean;

import java.util.List;
import java.util.stream.Stream;

/**
 * 首页推荐列表
 * Created by lijipei on 2016/12/9.
 */

public class HomeGoodsBean extends BaseBean{

    private List<HomeGoodsBean> listData; //推荐商品集合

    public List<HomeGoodsBean> getListData() {
        return listData;
    }

    public void setListData(List<HomeGoodsBean> listData) {
        this.listData = listData;
    }
    private String shp_id;//id
    private  String shp_title;//标题
    private String shp_type;//类型  1 分类 2 标签 3 商品
    private String shp_background_show;//边框
    private String shp_url_show;//显示商品图片
    private String gg_tittle;//商品标题
    private String goods_price;//商品价格
    private String shp_ggp_id;//gpid
    private String unitid_nameref;

    public String getUnitid_nameref() {
        return unitid_nameref;
    }

    public void setUnitid_nameref(String unitid_nameref) {
        this.unitid_nameref = unitid_nameref;
    }

    public String getShp_id() {
        return shp_id;
    }

    public void setShp_id(String shp_id) {
        this.shp_id = shp_id;
    }

    public String getShp_title() {
        return shp_title;
    }

    public void setShp_title(String shp_title) {
        this.shp_title = shp_title;
    }

    public String getShp_type() {
        return shp_type;
    }

    public void setShp_type(String shp_type) {
        this.shp_type = shp_type;
    }

    public String getShp_background_show() {
        return shp_background_show;
    }

    public void setShp_background_show(String shp_background_show) {
        this.shp_background_show = shp_background_show;
    }

    public String getShp_url_show() {
        return shp_url_show;
    }

    public void setShp_url_show(String shp_url_show) {
        this.shp_url_show = shp_url_show;
    }

    public String getGg_tittle() {
        return gg_tittle;
    }

    public void setGg_tittle(String gg_tittle) {
        this.gg_tittle = gg_tittle;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getShp_ggp_id() {
        return shp_ggp_id;
    }

    public void setShp_ggp_id(String shp_ggp_id) {
        this.shp_ggp_id = shp_ggp_id;
    }

    /*private String whp_id;//	id
    private String whp_title;//	标题
    private String whp_shopid;//	店铺id
    private String whp_goodsid;//	商品id
    private String gb_cover;//	封面
    private String gb_title;//	商品标题
    private String goods_price;//	商品价格
    private String gp_id;//	gp_id
    private String skip_type;//	1 列表 2 单个商品
    private String backgroundImage;//推荐商品背景图

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getSkip_type() {
        return skip_type;
    }

    public void setSkip_type(String skip_type) {
        this.skip_type = skip_type;
    }

    public String getWhp_id() {
        return whp_id;
    }

    public void setWhp_id(String whp_id) {
        this.whp_id = whp_id;
    }

    public String getWhp_title() {
        return whp_title;
    }

    public void setWhp_title(String whp_title) {
        this.whp_title = whp_title;
    }

    public String getWhp_shopid() {
        return whp_shopid;
    }

    public void setWhp_shopid(String whp_shopid) {
        this.whp_shopid = whp_shopid;
    }

    public String getWhp_goodsid() {
        return whp_goodsid;
    }

    public void setWhp_goodsid(String whp_goodsid) {
        this.whp_goodsid = whp_goodsid;
    }

    public String getGb_cover() {
        return gb_cover;
    }

    public void setGb_cover(String gb_cover) {
        this.gb_cover = gb_cover;
    }

    public String getGb_title() {
        return gb_title;
    }

    public void setGb_title(String gb_title) {
        this.gb_title = gb_title;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGp_id() {
        return gp_id;
    }

    public void setGp_id(String gp_id) {
        this.gp_id = gp_id;
    }*/
}
