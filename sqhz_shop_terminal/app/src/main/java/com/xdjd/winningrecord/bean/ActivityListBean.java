package com.xdjd.winningrecord.bean;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ActivityListBean extends BaseBean {

    private String num;
    private List<ActivityListBean> listData;//

    private String mm_id;//	活动id
    private String mm_name;//	活动名称
    private String mm_type_nameref;//	活动类型
    private String mm_startime = "";//	活动开始时间
    private String mm_endtime = "";//	活动结束时间
    private String mm_prize_totalnum = "0";//	奖品总数量
    private String mm_prize_currentnum = "0";//	剩余奖品数量
    private String mm_prize_grantnum = "0";//已发放的奖品数量
    private String mm_participant_num = "0";//	参与人
    private String mm_spread_num = "0";//	传播人数
    private String mm_participant_num_max = "0";//	最大参与人数
    private String area_range;//范围
    private String background_image;

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getArea_range() {
        return area_range;
    }

    public void setArea_range(String area_range) {
        this.area_range = area_range;
    }

    public String getMm_prize_grantnum() {
        return mm_prize_grantnum;
    }

    public void setMm_prize_grantnum(String mm_prize_grantnum) {
        this.mm_prize_grantnum = mm_prize_grantnum;
    }

    public List<ActivityListBean> getListData() {
        return listData;
    }

    public void setListData(List<ActivityListBean> listData) {
        this.listData = listData;
    }

    public String getMm_id() {
        return mm_id;
    }

    public void setMm_id(String mm_id) {
        this.mm_id = mm_id;
    }

    public String getMm_name() {
        return mm_name;
    }

    public void setMm_name(String mm_name) {
        this.mm_name = mm_name;
    }

    public String getMm_type_nameref() {
        return mm_type_nameref;
    }

    public void setMm_type_nameref(String mm_type_nameref) {
        this.mm_type_nameref = mm_type_nameref;
    }

    public String getMm_startime() {
        return mm_startime;
    }

    public void setMm_startime(String mm_startime) {
        this.mm_startime = mm_startime;
    }

    public String getMm_endtime() {
        return mm_endtime;
    }

    public void setMm_endtime(String mm_endtime) {
        this.mm_endtime = mm_endtime;
    }

    public String getMm_prize_totalnum() {
        return mm_prize_totalnum;
    }

    public void setMm_prize_totalnum(String mm_prize_totalnum) {
        this.mm_prize_totalnum = mm_prize_totalnum;
    }

    public String getMm_prize_currentnum() {
        return mm_prize_currentnum;
    }

    public void setMm_prize_currentnum(String mm_prize_currentnum) {
        this.mm_prize_currentnum = mm_prize_currentnum;
    }

    public String getMm_participant_num() {
        return mm_participant_num;
    }

    public void setMm_participant_num(String mm_participant_num) {
        this.mm_participant_num = mm_participant_num;
    }

    public String getMm_spread_num() {
        return mm_spread_num;
    }

    public void setMm_spread_num(String mm_spread_num) {
        this.mm_spread_num = mm_spread_num;
    }

    public String getMm_participant_num_max() {
        return mm_participant_num_max;
    }

    public void setMm_participant_num_max(String mm_participant_num_max) {
        this.mm_participant_num_max = mm_participant_num_max;
    }
}
