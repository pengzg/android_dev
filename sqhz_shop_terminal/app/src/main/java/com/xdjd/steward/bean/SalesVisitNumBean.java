package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SalesVisitNumBean extends BaseBean{

    private List<SalesVisitNumBean> dataList;//	商品列表
    private String clct_userid_nameref = "";//	业务员名
    private float visitNum = 0;//	拜访量
    private String clct_userid;//	业务员id

    public String getClct_userid() {
        return clct_userid;
    }

    public void setClct_userid(String clct_userid) {
        this.clct_userid = clct_userid;
    }

    public List<SalesVisitNumBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<SalesVisitNumBean> dataList) {
        this.dataList = dataList;
    }

    public String getClct_userid_nameref() {
        return clct_userid_nameref;
    }

    public void setClct_userid_nameref(String clct_userid_nameref) {
        this.clct_userid_nameref = clct_userid_nameref;
    }

    public float getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(float visitNum) {
        this.visitNum = visitNum;
    }
}
