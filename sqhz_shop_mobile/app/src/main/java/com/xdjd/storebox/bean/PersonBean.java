package com.xdjd.storebox.bean;

import java.util.Set;
import java.util.stream.Stream;

/**
 * 用户信息bean
 * Created by lijipei on 2017/1/3.
 */

public class PersonBean extends BaseBean {
    private String cc_address;//客户地址
    private String cc_contacts_name;//	呢称
    private String birthDay;//	生日
    private String cc_contacts_mobile;//	手机
    private String cc_name;//	用户店铺
    private String shopTel;//	店铺电话
    private String cc_image;//	用户头像
    private String unionId;//	微信唯一id
    private String spreadMobile;//	推广员电话
    private String spread_name;//	推广员姓名
    private String centerShopName;//	中心仓店铺名
    private String shopId;//	中心仓id
    private Set<String> tagList;//	标签列表
    private String appdown_url; //下载H5界面
    private String userType;//用户类型

    private String userShopId;//	用户店铺id

    private String is_integrate_show;//是否显示积分
    private String integrate;//积分
    private String inte_rule_url;//积分规则
    private String beat_percent;//打败的百分比
    private String unread_message_nums;//未读的消息条数
    private String favorite_nums;//收藏商品条数
    private String cc_orgid_nameref;//公司名称

    public String getCc_address() {
        return cc_address;
    }

    public void setCc_address(String cc_address) {
        this.cc_address = cc_address;
    }

    public String getCc_orgid_nameref() {
        return cc_orgid_nameref;
    }

    public void setCc_orgid_nameref(String cc_orgid_nameref) {
        this.cc_orgid_nameref = cc_orgid_nameref;
    }

    public String getShopTel() {
        return shopTel;
    }

    public void setShopTel(String shopTel) {
        this.shopTel = shopTel;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contacts_name) {
        this.cc_contacts_name = cc_contacts_name;
    }

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCc_image() {
        return cc_image;
    }

    public void setCc_image(String cc_image) {
        this.cc_image = cc_image;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getSpreadMobile() {
        return spreadMobile;
    }

    public void setSpreadMobile(String spreadMobile) {
        this.spreadMobile = spreadMobile;
    }

    public String getSpread_name() {
        return spread_name;
    }

    public void setSpread_name(String spread_name) {
        this.spread_name = spread_name;
    }

    public String getCenterShopName() {
        return centerShopName;
    }

    public void setCenterShopName(String centerShopName) {
        this.centerShopName = centerShopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Set<String> getTagList() {
        return tagList;
    }

    public void setTagList(Set<String> tagList) {
        this.tagList = tagList;
    }

    public String getAppdown_url() {
        return appdown_url;
    }

    public void setAppdown_url(String appdown_url) {
        this.appdown_url = appdown_url;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserShopId() {
        return userShopId;
    }

    public void setUserShopId(String userShopId) {
        this.userShopId = userShopId;
    }

    public String getIs_integrate_show() {
        return is_integrate_show;
    }

    public void setIs_integrate_show(String is_integrate_show) {
        this.is_integrate_show = is_integrate_show;
    }

    public String getIntegrate() {
        return integrate;
    }

    public void setIntegrate(String integrate) {
        this.integrate = integrate;
    }

    public String getInte_rule_url() {
        return inte_rule_url;
    }

    public void setInte_rule_url(String inte_rule_url) {
        this.inte_rule_url = inte_rule_url;
    }

    public String getBeat_percent() {
        return beat_percent;
    }

    public void setBeat_percent(String beat_percent) {
        this.beat_percent = beat_percent;
    }

    public String getUnread_message_nums() {
        return unread_message_nums;
    }

    public void setUnread_message_nums(String unread_message_nums) {
        this.unread_message_nums = unread_message_nums;
    }

    public String getFavorite_nums() {
        return favorite_nums;
    }

    public void setFavorite_nums(String favorite_nums) {
        this.favorite_nums = favorite_nums;
    }
}
