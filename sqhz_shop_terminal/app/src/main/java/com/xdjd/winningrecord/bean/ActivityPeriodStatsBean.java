package com.xdjd.winningrecord.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.Comparator;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ActivityPeriodStatsBean extends BaseBean{
    private List<ActivityPeriodStatsBean> cjList;//	日抽奖数列表
    private List<ActivityPeriodStatsBean> zjList;//	日中奖数列表
    private List<ActivityPeriodStatsBean> djList;//	日兑奖数列表

    private String sc_date;//	日期
    private String sc_year;	//年
    private String sc_month;	//月
    private String sc_day;	//日
    private int statsNum;	//统计数量

    public List<ActivityPeriodStatsBean> getCjList() {
        return cjList;
    }

    public void setCjList(List<ActivityPeriodStatsBean> cjList) {
        this.cjList = cjList;
    }

    public List<ActivityPeriodStatsBean> getZjList() {
        return zjList;
    }

    public void setZjList(List<ActivityPeriodStatsBean> zjList) {
        this.zjList = zjList;
    }

    public List<ActivityPeriodStatsBean> getDjList() {
        return djList;
    }

    public void setDjList(List<ActivityPeriodStatsBean> djList) {
        this.djList = djList;
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

    public int getStatsNum() {
        return statsNum;
    }

    public void setStatsNum(int statsNum) {
        this.statsNum = statsNum;
    }
}
