package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitingAlarmBean extends BaseBean {

    private List<VisitingAlarmBean> dataList;//	客户列表
    private String total = "0";

    private String cc_name;//	客户名称
    private String cc_id;//	客户id
    private String cc_last_visiting_date;//	上次拜访时间
    private String cc_address;//	地址
    private String last_visit_days;//	离最后一次拜访天数
    private String expire_days;//	超期天数
    private String cc_contacts_mobile;//	联系电话
    private String cc_contacts_name;//	联系人
    private String cc_visiting_cycle;//	拜访周期


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCc_contacts_name() {
        return cc_contacts_name;
    }

    public void setCc_contacts_name(String cc_contacts_name) {
        this.cc_contacts_name = cc_contacts_name;
    }

    public List<VisitingAlarmBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<VisitingAlarmBean> dataList) {
        this.dataList = dataList;
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

    public String getCc_last_visiting_date() {
        return cc_last_visiting_date;
    }

    public void setCc_last_visiting_date(String cc_last_visiting_date) {
        this.cc_last_visiting_date = cc_last_visiting_date;
    }

    public String getCc_address() {
        return cc_address;
    }

    public void setCc_address(String cc_address) {
        this.cc_address = cc_address;
    }

    public String getLast_visit_days() {
        return last_visit_days;
    }

    public void setLast_visit_days(String last_visit_days) {
        this.last_visit_days = last_visit_days;
    }

    public String getExpire_days() {
        return expire_days;
    }

    public void setExpire_days(String expire_days) {
        this.expire_days = expire_days;
    }

    public String getCc_contacts_mobile() {
        return cc_contacts_mobile;
    }

    public void setCc_contacts_mobile(String cc_contacts_mobile) {
        this.cc_contacts_mobile = cc_contacts_mobile;
    }

    public String getCc_visiting_cycle() {
        return cc_visiting_cycle;
    }

    public void setCc_visiting_cycle(String cc_visiting_cycle) {
        this.cc_visiting_cycle = cc_visiting_cycle;
    }
}
