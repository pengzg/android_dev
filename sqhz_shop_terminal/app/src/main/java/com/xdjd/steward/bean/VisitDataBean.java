package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitDataBean extends BaseBean {

    private String dayDealNum = "0.00";//	今天成交量
    private int dayVisitNum = 0;//	今天拜访数
    private String monthVisitNum = "0.00";//	月拜访数
    private String monthDealNum = "0.00";//	月成交

    private float amount = 0.00f;//成交总金额

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDayDealNum() {
        return dayDealNum;
    }

    public void setDayDealNum(String dayDealNum) {
        this.dayDealNum = dayDealNum;
    }

    public int getDayVisitNum() {
        return dayVisitNum;
    }

    public void setDayVisitNum(int dayVisitNum) {
        this.dayVisitNum = dayVisitNum;
    }

    public String getMonthVisitNum() {
        return monthVisitNum;
    }

    public void setMonthVisitNum(String monthVisitNum) {
        this.monthVisitNum = monthVisitNum;
    }

    public String getMonthDealNum() {
        return monthDealNum;
    }

    public void setMonthDealNum(String monthDealNum) {
        this.monthDealNum = monthDealNum;
    }
}
