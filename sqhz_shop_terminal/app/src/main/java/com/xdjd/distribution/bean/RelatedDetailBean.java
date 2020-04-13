package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   : 店铺关联
 *     version: 1.0
 * </pre>
 */

public class RelatedDetailBean extends BaseBean implements Serializable {

    private String mmu_memberid_nameref;//	店铺关联的核销员姓名
    private String mmu_memberid;
    private List<EquipmentBean> listData;//	未关联店铺的设备列表
    private List<EquipmentBean> dataList;//	店铺关联的设备列表	列表字段同listData

    private String me_num;//	设备序列号
    private String me_type_nameref;//	设备类型
    private String me_state_nameref;//	激活状态
    private String me_id;

    public String getMmu_memberid_nameref() {
        return mmu_memberid_nameref;
    }

    public void setMmu_memberid_nameref(String mmu_memberid_nameref) {
        this.mmu_memberid_nameref = mmu_memberid_nameref;
    }

    public String getMmu_memberid() {
        return mmu_memberid;
    }

    public void setMmu_memberid(String mmu_memberid) {
        this.mmu_memberid = mmu_memberid;
    }

    public List<EquipmentBean> getListData() {
        return listData;
    }

    public void setListData(List<EquipmentBean> listData) {
        this.listData = listData;
    }

    public List<EquipmentBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<EquipmentBean> dataList) {
        this.dataList = dataList;
    }

    public String getMe_num() {
        return me_num;
    }

    public void setMe_num(String me_num) {
        this.me_num = me_num;
    }

    public String getMe_type_nameref() {
        return me_type_nameref;
    }

    public void setMe_type_nameref(String me_type_nameref) {
        this.me_type_nameref = me_type_nameref;
    }

    public String getMe_state_nameref() {
        return me_state_nameref;
    }

    public void setMe_state_nameref(String me_state_nameref) {
        this.me_state_nameref = me_state_nameref;
    }

    public String getMe_id() {
        return me_id;
    }

    public void setMe_id(String me_id) {
        this.me_id = me_id;
    }
}
