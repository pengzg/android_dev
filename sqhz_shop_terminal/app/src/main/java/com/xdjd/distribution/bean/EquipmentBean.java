package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class EquipmentBean extends BaseBean {

    private List<EquipmentBean> listData;//	返回list

    private String me_num;//	设备序列号
    private String me_type_nameref;//	设备类型
    private String me_state_nameref;//	激活状态
    private String me_id;//

    public List<EquipmentBean> getListData() {
        return listData;
    }

    public void setListData(List<EquipmentBean> listData) {
        this.listData = listData;
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
