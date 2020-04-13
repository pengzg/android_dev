package com.xdjd.storebox.bean;

/**
 * 筛选bean
 * Created by lijipei on 2016/11/27.
 */

public class ScreenBean {

    private String gcId;//	分类id
    private String gcCode;//	分类code
    private String gcName;//	分类名称

    private String brandId;//	品牌id
    private String brandName;//	品牌名称

    private String bd_code;//	价格id
    private String bd_name;//	价格展示内容

    private int type;//1:是选中; 0是未选中

    public String getGcCode() {
        return gcCode;
    }

    public void setGcCode(String gcCode) {
        this.gcCode = gcCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGcId() {
        return gcId;
    }

    public void setGcId(String gcId) {
        this.gcId = gcId;
    }

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBd_code() {
        return bd_code;
    }

    public void setBd_code(String bd_code) {
        this.bd_code = bd_code;
    }

    public String getBd_name() {
        return bd_name;
    }

    public void setBd_name(String bd_name) {
        this.bd_name = bd_name;
    }
}
