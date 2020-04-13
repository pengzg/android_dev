package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MemberBean extends BaseBean implements Serializable{

    private int num;//	数量
    private List<MemberBean> listData;//	返回list

    private String mb_nickname;//	会员昵称
    private String mb_name;//	会员姓名
    private String mb_img;//	图像
    private String mb_mobile;//	手机号
    private String mb_city;//	城市
    private String mb_sex_nameref;//	性别
    private String mb_id;//


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<MemberBean> getListData() {
        return listData;
    }

    public void setListData(List<MemberBean> listData) {
        this.listData = listData;
    }

    public String getMb_nickname() {
        return mb_nickname;
    }

    public void setMb_nickname(String mb_nickname) {
        this.mb_nickname = mb_nickname;
    }

    public String getMb_name() {
        return mb_name;
    }

    public void setMb_name(String mb_name) {
        this.mb_name = mb_name;
    }

    public String getMb_img() {
        return mb_img;
    }

    public void setMb_img(String mb_img) {
        this.mb_img = mb_img;
    }

    public String getMb_mobile() {
        return mb_mobile;
    }

    public void setMb_mobile(String mb_mobile) {
        this.mb_mobile = mb_mobile;
    }

    public String getMb_city() {
        return mb_city;
    }

    public void setMb_city(String mb_city) {
        this.mb_city = mb_city;
    }

    public String getMb_sex_nameref() {
        return mb_sex_nameref;
    }

    public void setMb_sex_nameref(String mb_sex_nameref) {
        this.mb_sex_nameref = mb_sex_nameref;
    }

    public String getMb_id() {
        return mb_id;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }
}
