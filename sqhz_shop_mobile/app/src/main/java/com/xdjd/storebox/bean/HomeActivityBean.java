package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/8/16.
 */

public class HomeActivityBean extends BaseBean {
    //首页活动的
    private String activityId;//	活动id
    private String activityName;//	活动标题
    private String activityCover;//	活动封面
    private String type;//活动类型 5：组合促销
    private List<HomeActivityBean> listData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityCover() {
        return activityCover;
    }

    public void setActivityCover(String activityCover) {
        this.activityCover = activityCover;
    }

    public List<HomeActivityBean> getListData() {
        return listData;
    }

    public void setListData(List<HomeActivityBean> listData) {
        this.listData = listData;
    }
}
