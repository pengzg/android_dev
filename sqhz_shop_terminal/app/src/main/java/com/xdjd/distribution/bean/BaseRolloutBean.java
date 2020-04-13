package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/3
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BaseRolloutBean extends BaseBean {
    private String om_ordercode = "";
    private String order_code ="";

    public String getOm_ordercode() {
        return om_ordercode;
    }

    public void setOm_ordercode(String om_ordercode) {
        this.om_ordercode = om_ordercode;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}
