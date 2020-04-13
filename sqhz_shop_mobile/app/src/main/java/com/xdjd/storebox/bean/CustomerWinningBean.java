package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2017/10/30.
 */

public class CustomerWinningBean extends BaseBean {

    private String total_nums;//	总数
    private List<CustomerWinningBean> listCustomer;//	返回list
    private List<CustomerWinningBean> listData;//	返回list

    private String mw_memberid;//	会员id
    private String mw_member_name;//	会员名称
    private String mw_nums;//	奖品数量

    private String mw_id;//	中奖id
    private String mw_prizecode;//	中奖编码
    private String mw_activity_name;//	活动名称
    private String mw_winningtime;//	中奖时间
    private String mw_productname;//	中奖商品名称
    private String mw_num;//	数量
    private String mw_nums_desc;//	数量
    private String mw_member_phone;//	会员手机
    private String mw_member_mb_img;//	会员头像
    private String mw_state_nameref;//	中奖状态
    private String mw_state;//	中奖状态

    private String mw_granttime;// 领奖时间

    public String getMw_granttime() {
        return mw_granttime;
    }

    public void setMw_granttime(String mw_granttime) {
        this.mw_granttime = mw_granttime;
    }

    public List<CustomerWinningBean> getListData() {
        return listData;
    }

    public void setListData(List<CustomerWinningBean> listData) {
        this.listData = listData;
    }

    public String getMw_id() {
        return mw_id;
    }

    public void setMw_id(String mw_id) {
        this.mw_id = mw_id;
    }

    public String getMw_prizecode() {
        return mw_prizecode;
    }

    public void setMw_prizecode(String mw_prizecode) {
        this.mw_prizecode = mw_prizecode;
    }

    public String getMw_activity_name() {
        return mw_activity_name;
    }

    public void setMw_activity_name(String mw_activity_name) {
        this.mw_activity_name = mw_activity_name;
    }

    public String getMw_winningtime() {
        return mw_winningtime;
    }

    public void setMw_winningtime(String mw_winningtime) {
        this.mw_winningtime = mw_winningtime;
    }

    public String getMw_productname() {
        return mw_productname;
    }

    public void setMw_productname(String mw_productname) {
        this.mw_productname = mw_productname;
    }

    public String getMw_num() {
        return mw_num;
    }

    public void setMw_num(String mw_num) {
        this.mw_num = mw_num;
    }

    public String getMw_nums_desc() {
        return mw_nums_desc;
    }

    public void setMw_nums_desc(String mw_nums_desc) {
        this.mw_nums_desc = mw_nums_desc;
    }

    public String getMw_member_phone() {
        return mw_member_phone;
    }

    public void setMw_member_phone(String mw_member_phone) {
        this.mw_member_phone = mw_member_phone;
    }

    public String getMw_member_mb_img() {
        return mw_member_mb_img;
    }

    public void setMw_member_mb_img(String mw_member_mb_img) {
        this.mw_member_mb_img = mw_member_mb_img;
    }

    public String getMw_state_nameref() {
        return mw_state_nameref;
    }

    public void setMw_state_nameref(String mw_state_nameref) {
        this.mw_state_nameref = mw_state_nameref;
    }

    public String getMw_state() {
        return mw_state;
    }

    public void setMw_state(String mw_state) {
        this.mw_state = mw_state;
    }

    public String getTotal_nums() {
        return total_nums;
    }

    public void setTotal_nums(String total_nums) {
        this.total_nums = total_nums;
    }

    public List<CustomerWinningBean> getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(List<CustomerWinningBean> listCustomer) {
        this.listCustomer = listCustomer;
    }

    public String getMw_memberid() {
        return mw_memberid;
    }

    public void setMw_memberid(String mw_memberid) {
        this.mw_memberid = mw_memberid;
    }

    public String getMw_member_name() {
        return mw_member_name;
    }

    public void setMw_member_name(String mw_member_name) {
        this.mw_member_name = mw_member_name;
    }

    public String getMw_nums() {
        return mw_nums;
    }

    public void setMw_nums(String mw_nums) {
        this.mw_nums = mw_nums;
    }
}
