package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/1/17.
 */

public class CartActionBean {

    private String activityId;//	活动id	0为无活动
    private String activityDesc;//	活动描述
    private String activityName;//	活动名称

    private String activityType;//	活动类型 	5 组合促销
    private String activityNum;//	活懂数量	5 组合促销
    private int isChild = 0; //是否选中,1是选中, 其他未选中

    private List<CartGoodsListBean> dataList;//	商品详情
    private List<GiftListBean> giftList;//	赠品列表

    public int getIsChild() {
        return isChild;
    }

    public void setIsChild(int isChild) {
        this.isChild = isChild;
    }

    public String getActivityNum() {
        return activityNum;
    }

    public void setActivityNum(String activityNum) {
        this.activityNum = activityNum;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public List<CartGoodsListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<CartGoodsListBean> dataList) {
        this.dataList = dataList;
    }

    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }
}
