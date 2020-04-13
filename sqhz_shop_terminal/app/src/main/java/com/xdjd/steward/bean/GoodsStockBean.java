package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsStockBean extends BaseBean {

    private List<GoodsStockBean> dataList;

    private String gg_title = "";//	商品名
    private String ggp_stock_desc = "0";//	库存
    private String ggs_storehouseid_nameref = "";//	仓库名
    private String gg_model = "";//	规格
    private String gg_simpletitle = "";//	商品简称
    private float ggp_stock = 0;//库存数量

    public float getGgp_stock() {
        return ggp_stock;
    }

    public void setGgp_stock(float ggp_stock) {
        this.ggp_stock = ggp_stock;
    }

    public String getGg_simpletitle() {
        return gg_simpletitle;
    }

    public void setGg_simpletitle(String gg_simpletitle) {
        this.gg_simpletitle = gg_simpletitle;
    }

    public List<GoodsStockBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<GoodsStockBean> dataList) {
        this.dataList = dataList;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getGgp_stock_desc() {
        return ggp_stock_desc;
    }

    public void setGgp_stock_desc(String ggp_stock_desc) {
        this.ggp_stock_desc = ggp_stock_desc;
    }

    public String getGgs_storehouseid_nameref() {
        return ggs_storehouseid_nameref;
    }

    public void setGgs_storehouseid_nameref(String ggs_storehouseid_nameref) {
        this.ggs_storehouseid_nameref = ggs_storehouseid_nameref;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }
}
