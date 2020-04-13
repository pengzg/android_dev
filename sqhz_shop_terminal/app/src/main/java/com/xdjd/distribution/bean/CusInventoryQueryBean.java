package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CusInventoryQueryBean extends BaseBean {

    private List<CusInventoryQueryBean> dataList;//	盘点列表

    private String cim_id;//	盘点id
    private String cim_customerid_nameref;//	客户名
    private String cim_customerid;//	盘点客户id
    private String cim_code;//	编码
    private String cim_billdate;//	日期
    private String cim_totalamount;//	总金额
    private String cim_totalnum;//	总数量
    private String cim_goodscount;//	商品条数
    private String cim_stats_nameref = "";//	审核状态

    public String getCim_stats_nameref() {
        return cim_stats_nameref;
    }

    public void setCim_stats_nameref(String cim_stats_nameref) {
        this.cim_stats_nameref = cim_stats_nameref;
    }

    public String getCim_goodscount() {
        return cim_goodscount;
    }

    public void setCim_goodscount(String cim_goodscount) {
        this.cim_goodscount = cim_goodscount;
    }

    public List<CusInventoryQueryBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CusInventoryQueryBean> dataList) {
        this.dataList = dataList;
    }

    public String getCim_id() {
        return cim_id;
    }

    public void setCim_id(String cim_id) {
        this.cim_id = cim_id;
    }

    public String getCim_customerid_nameref() {
        return cim_customerid_nameref;
    }

    public void setCim_customerid_nameref(String cim_customerid_nameref) {
        this.cim_customerid_nameref = cim_customerid_nameref;
    }

    public String getCim_customerid() {
        return cim_customerid;
    }

    public void setCim_customerid(String cim_customerid) {
        this.cim_customerid = cim_customerid;
    }

    public String getCim_code() {
        return cim_code;
    }

    public void setCim_code(String cim_code) {
        this.cim_code = cim_code;
    }

    public String getCim_billdate() {
        return cim_billdate;
    }

    public void setCim_billdate(String cim_billdate) {
        this.cim_billdate = cim_billdate;
    }

    public String getCim_totalamount() {
        return cim_totalamount;
    }

    public void setCim_totalamount(String cim_totalamount) {
        this.cim_totalamount = cim_totalamount;
    }

    public String getCim_totalnum() {
        return cim_totalnum;
    }

    public void setCim_totalnum(String cim_totalnum) {
        this.cim_totalnum = cim_totalnum;
    }
}
