package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/10/30.
 */

public class GiftWinningBean extends BaseBean {
    private String total_nums;//	总数
    private List<GiftWinningBean> listData;//	返回list

    private String mw_prizeid;//	奖项id
    private String mw_productid;//	奖品id
    private String mw_productname;//	奖品名称
    private String mw_nums;//	奖品数量
    private String mw_nums_desc;//	奖品数量

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public String getMw_nums_desc() {
        return mw_nums_desc;
    }

    public void setMw_nums_desc(String mw_nums_desc) {
        this.mw_nums_desc = mw_nums_desc;
    }

    public List<GiftWinningBean> getListData() {
        return listData;
    }

    public void setListData(List<GiftWinningBean> listData) {
        this.listData = listData;
    }

    public String getMw_prizeid() {
        return mw_prizeid;
    }

    public void setMw_prizeid(String mw_prizeid) {
        this.mw_prizeid = mw_prizeid;
    }

    public String getMw_productid() {
        return mw_productid;
    }

    public void setMw_productid(String mw_productid) {
        this.mw_productid = mw_productid;
    }

    public String getMw_productname() {
        return mw_productname;
    }

    public void setMw_productname(String mw_productname) {
        this.mw_productname = mw_productname;
    }

    public String getMw_nums() {
        return mw_nums;
    }

    public void setMw_nums(String mw_nums) {
        this.mw_nums = mw_nums;
    }
}
