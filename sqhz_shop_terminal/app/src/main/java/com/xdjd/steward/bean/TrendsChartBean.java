package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class TrendsChartBean extends BaseBean {

    private List<TrendsChartBean> dataList;//	商品列表
    private List<TrendsChartBean> unFinishListData;//	全部

    private float amount = 0;//	金额
    private String dayStr = "";//	日
    private String monthStr = "";//	月
    private String yearStr = "";//	年
    private int orderNum = 0;//	订单数

    private float orderAmount;//	全部金额

    public float getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(float orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<TrendsChartBean> getUnFinishListData() {
        return unFinishListData;
    }

    public void setUnFinishListData(List<TrendsChartBean> unFinishListData) {
        this.unFinishListData = unFinishListData;
    }

    public List<TrendsChartBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<TrendsChartBean> dataList) {
        this.dataList = dataList;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public String getMonthStr() {
        return monthStr;
    }

    public void setMonthStr(String monthStr) {
        this.monthStr = monthStr;
    }

    public String getYearStr() {
        return yearStr;
    }

    public void setYearStr(String yearStr) {
        this.yearStr = yearStr;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
