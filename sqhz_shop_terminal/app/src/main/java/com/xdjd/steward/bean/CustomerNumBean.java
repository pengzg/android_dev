package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerNumBean extends BaseBean {
    private int newCustomerNum;//	新增客户数量
    private int totalCustomerNum;//	客户总数量

    public int getNewCustomerNum() {
        return newCustomerNum;
    }

    public void setNewCustomerNum(int newCustomerNum) {
        this.newCustomerNum = newCustomerNum;
    }

    public int getTotalCustomerNum() {
        return totalCustomerNum;
    }

    public void setTotalCustomerNum(int totalCustomerNum) {
        this.totalCustomerNum = totalCustomerNum;
    }
}
