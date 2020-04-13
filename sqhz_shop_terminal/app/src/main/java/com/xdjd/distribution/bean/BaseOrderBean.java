package com.xdjd.distribution.bean;

import java.io.Serializable;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/5
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BaseOrderBean extends BaseBean implements Serializable{
    private String order_code;//订单号

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}
