package com.xdjd.winningrecord.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserMonthlyStatsBean extends BaseBean {

    private List<UserMonthlyStatsBean> addList;//	当月日新增数列表
    private List<UserMonthlyStatsBean>activeList;//	当月日活跃数列表
    private List<UserMonthlyStatsBean>totalList;//	当月累计用户数列表

    private String sc_date;//	日期
    private String sc_year;//	年
    private String sc_month;//	月
    private String sc_day;//	日
    private int newNum;//	统计数量

    public List<UserMonthlyStatsBean> getAddList() {
        return addList;
    }

    public void setAddList(List<UserMonthlyStatsBean> addList) {
        this.addList = addList;
    }

    public List<UserMonthlyStatsBean> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<UserMonthlyStatsBean> activeList) {
        this.activeList = activeList;
    }

    public List<UserMonthlyStatsBean> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<UserMonthlyStatsBean> totalList) {
        this.totalList = totalList;
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

    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }
}
