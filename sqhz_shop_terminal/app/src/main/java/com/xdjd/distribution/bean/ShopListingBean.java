package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ShopListingBean extends BaseBean {

    private String totalNum = "0";//总的数量
    private List<ShopListingBean> listData;//	已关联设备的店铺列表

    private String ms_id;//	店铺id
    private String ms_img;//	店铺图像
    private String ms_name;//	店铺名称
    private String ms_mobile;//	店铺手机号
    private String ms_address;//	店铺地址
    private String mmu_pwd;//	核销密码


    public String getMmu_pwd() {
        return mmu_pwd;
    }

    public void setMmu_pwd(String mmu_pwd) {
        this.mmu_pwd = mmu_pwd;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public List<ShopListingBean> getListData() {
        return listData;
    }

    public void setListData(List<ShopListingBean> listData) {
        this.listData = listData;
    }

    public String getMs_id() {
        return ms_id;
    }

    public void setMs_id(String ms_id) {
        this.ms_id = ms_id;
    }

    public String getMs_img() {
        return ms_img;
    }

    public void setMs_img(String ms_img) {
        this.ms_img = ms_img;
    }

    public String getMs_name() {
        return ms_name;
    }

    public void setMs_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public String getMs_mobile() {
        return ms_mobile;
    }

    public void setMs_mobile(String ms_mobile) {
        this.ms_mobile = ms_mobile;
    }

    public String getMs_address() {
        return ms_address;
    }

    public void setMs_address(String ms_address) {
        this.ms_address = ms_address;
    }
}
