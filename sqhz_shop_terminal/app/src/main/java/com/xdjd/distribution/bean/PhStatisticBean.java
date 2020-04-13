package com.xdjd.distribution.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by freestyle_hong on 2017/12/19.
 */

public class PhStatisticBean extends BaseBean{
    private float phZje=0.00f;//铺货总金额
    private float yxsje=0.00f;//已销售金额
    private float wxsje=0.00f;//未销售金额
    private float thsje=0.00f;//撤货金额

    private List<PhStatisticBean> addList;//新增店铺数列表
    private List<PhStatisticBean> totalList;//累计店铺数列表
    private Integer addNum;//新增店铺数
    private Integer totalNum;//累计店铺数
    private String sc_date;//	日期
    private String sc_year;//	年
    private String sc_month;//	月
    private String sc_day;//	日


    public float getThsje() {
        return thsje;
    }

    public void setThsje(float thsje) {
        this.thsje = thsje;
    }

    public float getPhZje() {
        return phZje;
    }

    public void setPhZje(float phZje) {
        this.phZje = phZje;
    }

    public float getYxsje() {
        return yxsje;
    }

    public void setYxsje(float yxsje) {
        this.yxsje = yxsje;
    }

    public float getWxsje() {
        return wxsje;
    }

    public void setWxsje(float wxsje) {
        this.wxsje = wxsje;
    }

    public List<PhStatisticBean> getAddList() {
        return addList;
    }

    public void setAddList(List<PhStatisticBean> addList) {
        this.addList = addList;
    }

    public List<PhStatisticBean> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<PhStatisticBean> totalList) {
        this.totalList = totalList;
    }

    public Integer getAddNum() {
        return addNum;
    }

    public void setAddNum(Integer addNum) {
        this.addNum = addNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getSc_date() {
        return sc_date;
    }

    public void setSc_date(String sc_date) {
        this.sc_date = sc_date;
    }

    public String getSc_year() {
        return sc_year;
    }

    public void setSc_year(String sc_year) {
        this.sc_year = sc_year;
    }

    public String getSc_month() {
        return sc_month;
    }

    public void setSc_month(String sc_month) {
        this.sc_month = sc_month;
    }

    public String getSc_day() {
        return sc_day;
    }

    public void setSc_day(String sc_day) {
        this.sc_day = sc_day;
    }
}
