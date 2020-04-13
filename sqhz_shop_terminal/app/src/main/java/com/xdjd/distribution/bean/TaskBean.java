package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/26
 *     desc   : 配送任务bean
 *     version: 1.0
 * </pre>
 */

public class TaskBean extends BaseBean {

    private List<TaskBean> dataList;//	订单商品列表

    private String om_salesid;//	业务员id
    private String count;//	订单数
    private String om_salesid_nameref;//	业务员名字

    private List<CustomerTaskBean> customerList;//	订单列表

    public String getOm_salesid() {
        return om_salesid;
    }

    public void setOm_salesid(String om_salesid) {
        this.om_salesid = om_salesid;
    }

    public List<CustomerTaskBean> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<CustomerTaskBean> customerList) {
        this.customerList = customerList;
    }

    public List<TaskBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<TaskBean> dataList) {
        this.dataList = dataList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOm_salesid_nameref() {
        return om_salesid_nameref;
    }

    public void setOm_salesid_nameref(String om_salesid_nameref) {
        this.om_salesid_nameref = om_salesid_nameref;
    }
}
