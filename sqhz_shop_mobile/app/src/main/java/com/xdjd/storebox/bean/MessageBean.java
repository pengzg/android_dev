package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/2/22.
 */

public class MessageBean extends BaseBean {

    private List<MessageBean> listData;

    //消息类别参数
    private String message_type;//	消息类型  1 系统消息 2 活动促销 3 物流消息
    private String message_type_nameref;//	消息名称
    private String message_icon;//	消息图标
    private String message_unread_num;//	消息未读数量
    private String new_message_time;// 	最新消息时间
    private String new_message_describe;//	最新消息描述

    //消息列表参数
    private String message_title;//	消息标题
    private String message_content;//	消息描述
    private String message_img_nameref;//	消息图片
    private String message_jump_type;//	消息跳转类型
    private String message_jump_value;// 	跳转值
    private String message_read_flag;//	是否已读
    private String message_time;//	消息时间
    private String message_detail_id;//	消息id
    private String message_jump_model;//	消息跳转模块	1商品详情 2活动页3首页标签分类推荐4订单详情
    private String message_is_expired;	//是否过期	1未过期 2 已过期

    public String getMessage_is_expired() {
        return message_is_expired;
    }

    public void setMessage_is_expired(String message_is_expired) {
        this.message_is_expired = message_is_expired;
    }

    public String getMessage_jump_model() {
        return message_jump_model;
    }

    public void setMessage_jump_model(String message_jump_model) {
        this.message_jump_model = message_jump_model;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_img_nameref() {
        return message_img_nameref;
    }

    public void setMessage_img_nameref(String message_img_nameref) {
        this.message_img_nameref = message_img_nameref;
    }

    public String getMessage_jump_type() {
        return message_jump_type;
    }

    public void setMessage_jump_type(String message_jump_type) {
        this.message_jump_type = message_jump_type;
    }

    public String getMessage_jump_value() {
        return message_jump_value;
    }

    public void setMessage_jump_value(String message_jump_value) {
        this.message_jump_value = message_jump_value;
    }

    public String getMessage_read_flag() {
        return message_read_flag;
    }

    public void setMessage_read_flag(String message_read_flag) {
        this.message_read_flag = message_read_flag;
    }

    public String getMessage_time() {
        return message_time;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public String getMessage_detail_id() {
        return message_detail_id;
    }

    public void setMessage_detail_id(String message_detail_id) {
        this.message_detail_id = message_detail_id;
    }

    public List<MessageBean> getListData() {
        return listData;
    }

    public void setListData(List<MessageBean> listData) {
        this.listData = listData;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_type_nameref() {
        return message_type_nameref;
    }

    public void setMessage_type_nameref(String message_type_nameref) {
        this.message_type_nameref = message_type_nameref;
    }

    public String getMessage_icon() {
        return message_icon;
    }

    public void setMessage_icon(String message_icon) {
        this.message_icon = message_icon;
    }

    public String getMessage_unread_num() {
        return message_unread_num;
    }

    public void setMessage_unread_num(String message_unread_num) {
        this.message_unread_num = message_unread_num;
    }

    public String getNew_message_time() {
        return new_message_time;
    }

    public void setNew_message_time(String new_message_time) {
        this.new_message_time = new_message_time;
    }

    public String getNew_message_describe() {
        return new_message_describe;
    }

    public void setNew_message_describe(String new_message_describe) {
        this.new_message_describe = new_message_describe;
    }
}
