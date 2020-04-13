package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MainStockBean extends BaseBean {

    private List<MainStockBean> listData;//	商品列表

    private String ggs_stock;//	商品可用 库存
    private String ggs_storehouseid_nameref;//	仓库名成

    private String gg_title;//	商品名称

    private String ggp_unit_type;//
    private String ggp_unit_num;//	商品换算关系

    private String gg_model;//	商品规格

    private String gg_unit_max_nameref;//	大单位名
    private String gg_unit_min_nameref;//	小单位名

    public String getGg_unit_max_nameref() {
        return gg_unit_max_nameref;
    }

    public void setGg_unit_max_nameref(String gg_unit_max_nameref) {
        this.gg_unit_max_nameref = gg_unit_max_nameref;
    }

    public String getGg_unit_min_nameref() {
        return gg_unit_min_nameref;
    }

    public void setGg_unit_min_nameref(String gg_unit_min_nameref) {
        this.gg_unit_min_nameref = gg_unit_min_nameref;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public List<MainStockBean> getListData() {
        return listData;
    }

    public void setListData(List<MainStockBean> listData) {
        this.listData = listData;
    }

    public String getGgs_stock() {
        return ggs_stock;
    }

    public void setGgs_stock(String ggs_stock) {
        this.ggs_stock = ggs_stock;
    }

    public String getGgs_storehouseid_nameref() {
        return ggs_storehouseid_nameref;
    }

    public void setGgs_storehouseid_nameref(String ggs_storehouseid_nameref) {
        this.ggs_storehouseid_nameref = ggs_storehouseid_nameref;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getGgp_unit_type() {
        return ggp_unit_type;
    }

    public void setGgp_unit_type(String ggp_unit_type) {
        this.ggp_unit_type = ggp_unit_type;
    }

    public String getGgp_unit_num() {
        return ggp_unit_num;
    }

    public void setGgp_unit_num(String ggp_unit_num) {
        this.ggp_unit_num = ggp_unit_num;
    }
}
