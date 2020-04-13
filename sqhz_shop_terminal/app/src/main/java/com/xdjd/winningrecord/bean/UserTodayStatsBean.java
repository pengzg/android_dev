package com.xdjd.winningrecord.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserTodayStatsBean extends BaseBean {

    private int addNum;//	日新增数
    private int dailyActiveNum;//	日活跃数
    //    monthlyActiveNum	月活跃数
    private int totalNum;//	累计用户数,今天之前的累计

    public int getAddNum() {
        return addNum;
    }

    public void setAddNum(int addNum) {
        this.addNum = addNum;
    }

    public int getDailyActiveNum() {
        return dailyActiveNum;
    }

    public void setDailyActiveNum(int dailyActiveNum) {
        this.dailyActiveNum = dailyActiveNum;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
