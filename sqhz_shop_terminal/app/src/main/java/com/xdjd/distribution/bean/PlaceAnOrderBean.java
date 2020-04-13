package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PlaceAnOrderBean extends BaseBean implements Serializable{

    private List<PlaceAnOrderBean> listData;//		订单数据集合

    private String oa_applycode;//		订单编号
    private String oa_remarks;//		备注
    private String oa_id;//		订单id
    private String oa_applydate;//		申请日期
    private String oa_customername;//		客户名称
    private String oa_sett_amount;//		金额
    private String oa_state; //订单状态
    private String oa_state_nameref;

    public String getOa_state_nameref() {
        return oa_state_nameref;
    }

    public void setOa_state_nameref(String oa_state_nameref) {
        this.oa_state_nameref = oa_state_nameref;
    }

    public String getOa_state() {
        return oa_state;
    }

    public void setOa_state(String oa_state) {
        this.oa_state = oa_state;
    }

    public List<PlaceAnOrderBean> getListData() {
        return listData;
    }

    public void setListData(List<PlaceAnOrderBean> listData) {
        this.listData = listData;
    }

    public String getOa_applycode() {
        return oa_applycode;
    }

    public void setOa_applycode(String oa_applycode) {
        this.oa_applycode = oa_applycode;
    }

    public String getOa_remarks() {
        return oa_remarks;
    }

    public void setOa_remarks(String oa_remarks) {
        this.oa_remarks = oa_remarks;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getOa_applydate() {
        return oa_applydate;
    }

    public void setOa_applydate(String oa_applydate) {
        this.oa_applydate = oa_applydate;
    }

    public String getOa_customername() {
        return oa_customername;
    }

    public void setOa_customername(String oa_customername) {
        this.oa_customername = oa_customername;
    }

    public String getOa_sett_amount() {
        return oa_sett_amount;
    }

    public void setOa_sett_amount(String oa_sett_amount) {
        this.oa_sett_amount = oa_sett_amount;
    }
}
