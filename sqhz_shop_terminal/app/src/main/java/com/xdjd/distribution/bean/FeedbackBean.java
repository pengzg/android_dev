package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/26
 *     desc   : 反馈列表bean
 *     version: 1.0
 * </pre>
 */

public class FeedbackBean extends BaseBean {

    private List<FeedbackBean> listData;//	反馈列表

    private String cf_customerid_nameref;//	名称
    private String cf_content;//	反馈内容
    private String cf_process_status_nameref;//	处理状态
    private String cf_process_userid_nameref;//	处理人
    private String cf_process_note;//	处理意见
    private String cf_addtime;//	反馈时间
    private String cf_process_time;//	处理时间

    public List<FeedbackBean> getListData() {
        return listData;
    }

    public void setListData(List<FeedbackBean> listData) {
        this.listData = listData;
    }

    public String getCf_customerid_nameref() {
        return cf_customerid_nameref;
    }

    public void setCf_customerid_nameref(String cf_customerid_nameref) {
        this.cf_customerid_nameref = cf_customerid_nameref;
    }

    public String getCf_content() {
        return cf_content;
    }

    public void setCf_content(String cf_content) {
        this.cf_content = cf_content;
    }

    public String getCf_process_status_nameref() {
        return cf_process_status_nameref;
    }

    public void setCf_process_status_nameref(String cf_process_status_nameref) {
        this.cf_process_status_nameref = cf_process_status_nameref;
    }

    public String getCf_process_userid_nameref() {
        return cf_process_userid_nameref;
    }

    public void setCf_process_userid_nameref(String cf_process_userid_nameref) {
        this.cf_process_userid_nameref = cf_process_userid_nameref;
    }

    public String getCf_process_note() {
        return cf_process_note;
    }

    public void setCf_process_note(String cf_process_note) {
        this.cf_process_note = cf_process_note;
    }

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
}
