package com.xdjd.storebox.bean;

import java.util.List;

/**
 * 首页轮播
 * Created by lijipei on 2016/12/9.
 */

public class HomeBean extends BaseBean {
    private List<HomeBean> listTopData;//	顶端的轮播图	轮播图list集合
    private String cover;//封面图
    private String skip_value;//值
    private String type;//	类型	1 h5页面 2 活动 3商品
    private List<HomePageListBean> listData;

    //首页活动的
    private String activityId;//	活动id
    private String activityName;//	活动标题
    private String activityCover;//	活动封面

    public class HomePageListBean {
        //首页几个按钮的结婚
        private String image;//	图片
        private String title;//	标题
        private int type;//	类型：1活动，2商品，3标签，4我常买，5我的收藏，6 新品推荐 9．h5页面

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public List<HomePageListBean> getListData() {
        return listData;
    }

    public void setListData(List<HomePageListBean> listData) {
        this.listData = listData;
    }

    public List<HomeBean> getListTopData() {
        return listTopData;
    }

    public void setListTopData(List<HomeBean> listTopData) {
        this.listTopData = listTopData;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSkip_value() {
        return skip_value;
    }

    public void setSkip_value(String skip_value) {
        this.skip_value = skip_value;
    }

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
    /*private String cover;//	封面图
    private String skip_value;//	值	跳h5时为链接，跳 app 时为id 跳转的值
    private String type;//	类型	1 h5页面 2 活动 3商品
    private String shopId;//	店铺id
    private String title;//H5标题*/



    /*private List<HomePageListBean> listData;//首页顶部按钮集合数据

    public class HomePageListBean{
        //首页几个按钮的结婚
        private String image;//	图片
        private String title;//	标题
        private int type;//	类型：1活动，2商品，3标签，4我常买，5我的收藏，6 新品推荐 9．h5页面

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HomePageListBean> getListData() {
        return listData;
    }

    public void setListData(List<HomePageListBean> listData) {
        this.listData = listData;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
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

    public List<HomeBean> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<HomeBean> datalist) {
        this.datalist = datalist;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSkip_value() {
        return skip_value;
    }

    public void setSkip_value(String skip_value) {
        this.skip_value = skip_value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }*/
}
