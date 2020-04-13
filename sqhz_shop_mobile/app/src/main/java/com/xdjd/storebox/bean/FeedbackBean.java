package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/1/17.
 */

public class FeedbackBean extends BaseBean {
    private List<FeedbackBean>listData;
    private String cf_content;//反馈问题
    private String cf_process_note;//问题回复
    private String cf_process_status_nameref;//处理状态
    private String cf_addtime;//反馈提交时间
    private String cf_process_time;//处理时间

    public String getCf_addtime() {
        return cf_addtime;
    }

    public void setCf_addtime(String cf_addtime) {
        this.cf_addtime = cf_addtime;
    }

    public String getCf_process_time() {
        return cf_process_time;
    }

    public void setCf_process_time(String cf_process_time) {
        this.cf_process_time = cf_process_time;
    }

    public String getCf_process_status_nameref() {
        return cf_process_status_nameref;
    }

    public void setCf_process_status_nameref(String cf_process_status_nameref) {
        this.cf_process_status_nameref = cf_process_status_nameref;
    }

    public List<FeedbackBean> getListData() {
        return listData;
    }

    public void setListData(List<FeedbackBean> listData) {
        this.listData = listData;
    }

    public String getCf_content() {
        return cf_content;
    }

    public void setCf_content(String cf_content) {
        this.cf_content = cf_content;
    }

    public String getCf_process_note() {
        return cf_process_note;
    }

    public void setCf_process_note(String cf_process_note) {
        this.cf_process_note = cf_process_note;
    }
}
