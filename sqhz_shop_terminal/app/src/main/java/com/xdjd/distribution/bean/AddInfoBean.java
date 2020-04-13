package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/3
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class AddInfoBean extends BaseBean implements Serializable{

    private List<AddInfoBean> categoryList;//	客户类别
    private List<AddInfoBean> channelList;//	渠道类别
    private List<AddInfoBean> priceGradeList;//	价格方案

    private String cc_name;//	客户名
    private String cc_id;//	客户id
    private String cc_code;//	编码


    private String cct_name;//	渠道
    private String cct_id;//	渠道id
    private String cct_code;//	渠道编码

    private String gpg_title;//	名称
    private String gpg_id;//	价格方案id
    private String gpg_discount;//	折扣

    private List<AddInfoBean> subList;//	子集	 字段一样


    public List<AddInfoBean> getPriceGradeList() {
        return priceGradeList;
    }

    public void setPriceGradeList(List<AddInfoBean> priceGradeList) {
        this.priceGradeList = priceGradeList;
    }

    public String getGpg_title() {
        return gpg_title;
    }

    public void setGpg_title(String gpg_title) {
        this.gpg_title = gpg_title;
    }

    public String getGpg_id() {
        return gpg_id;
    }

    public void setGpg_id(String gpg_id) {
        this.gpg_id = gpg_id;
    }

    public String getGpg_discount() {
        return gpg_discount;
    }

    public void setGpg_discount(String gpg_discount) {
        this.gpg_discount = gpg_discount;
    }

    public String getCct_name() {
        return cct_name;
    }

    public void setCct_name(String cct_name) {
        this.cct_name = cct_name;
    }

    public String getCct_id() {
        return cct_id;
    }

    public void setCct_id(String cct_id) {
        this.cct_id = cct_id;
    }

    public String getCct_code() {
        return cct_code;
    }

    public void setCct_code(String cct_code) {
        this.cct_code = cct_code;
    }

    public List<AddInfoBean> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<AddInfoBean> categoryList) {
        this.categoryList = categoryList;
    }

    public List<AddInfoBean> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<AddInfoBean> channelList) {
        this.channelList = channelList;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public List<AddInfoBean> getSubList() {
        return subList;
    }

    public void setSubList(List<AddInfoBean> subList) {
        this.subList = subList;
    }
}
