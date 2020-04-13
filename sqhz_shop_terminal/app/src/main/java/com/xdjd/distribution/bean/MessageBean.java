package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MessageBean extends BaseBean {

    private List<MessageBean> listData;

    private String mp_id;
    //消息标题
    private String mp_title;
    //消息内容
    private String mp_content;
    //跳转类型 1 h5 2 活动 3 商品 4 消息详情 5订单6配送任务
    private String mp_jump_type;
    //消息跳转模块  1 app 2 h5页面
    private String mp_jump_model;
    //跳转值
    private String mp_jump_value;
    //是否已读 0 未读 1 已读
    private String mp_read_flag;
    //消息时间
    private String mp_add_time;

    public List<MessageBean> getListData() {
        return listData;
    }

    public void setListData(List<MessageBean> listData) {
        this.listData = listData;
    }

    public String getMp_id() {
        return mp_id;
    }

    public void setMp_id(String mp_id) {
        this.mp_id = mp_id;
    }

    public String getMp_title() {
        return mp_title;
    }

    public void setMp_title(String mp_title) {
        this.mp_title = mp_title;
    }

    public String getMp_content() {
        return mp_content;
    }

    public void setMp_content(String mp_content) {
        this.mp_content = mp_content;
    }

    public String getMp_jump_type() {
        return mp_jump_type;
    }

    public void setMp_jump_type(String mp_jump_type) {
        this.mp_jump_type = mp_jump_type;
    }

    public String getMp_jump_model() {
        return mp_jump_model;
    }

    public void setMp_jump_model(String mp_jump_model) {
        this.mp_jump_model = mp_jump_model;
    }

    public String getMp_jump_value() {
        return mp_jump_value;
    }

    public void setMp_jump_value(String mp_jump_value) {
        this.mp_jump_value = mp_jump_value;
    }

    public String getMp_read_flag() {
        return mp_read_flag;
    }

    public void setMp_read_flag(String mp_read_flag) {
        this.mp_read_flag = mp_read_flag;
    }

    public String getMp_add_time() {
        return mp_add_time;
    }

    public void setMp_add_time(String mp_add_time) {
        this.mp_add_time = mp_add_time;
    }
}
