package com.xdjd.distribution.event;

import com.xdjd.distribution.bean.AddressListBean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/6
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerAddressListEvent {

    private AddressListBean bean;//通讯录客户参数
    private int index;//集合下标

    public CustomerAddressListEvent(AddressListBean bean, int index) {
        this.bean = bean;
        this.index = index;
    }

    public AddressListBean getBean() {
        return bean;
    }

    public int getIndex() {
        return index;
    }
}
