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

public class ActivityTodayStatsBean extends BaseBean {
    private int cjNum;//	累计抽奖数
    private int zjNum;//	累计中奖数
    private int djNum;//	累计兑奖数

    public int getCjNum() {
        return cjNum;
    }

    public void setCjNum(int cjNum) {
        this.cjNum = cjNum;
    }

    public int getZjNum() {
        return zjNum;
    }

    public void setZjNum(int zjNum) {
        this.zjNum = zjNum;
    }

    public int getDjNum() {
        return djNum;
    }

    public void setDjNum(int djNum) {
        this.djNum = djNum;
    }
}
