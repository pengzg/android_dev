package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/5
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerBalanceBean extends BaseBean {
    private String balance;//	余额	 如果签到则返回 缓存到本地

    private String gcb_after_amount = "0.00";//	应收金额	 如果签到则返回 缓存到本地
    private String cc_safety_arrears_num = "0.00";//	安全欠款金额	 如果签到则返回 缓存到本地

    public String getGcb_after_amount() {
        return gcb_after_amount;
    }

    public void setGcb_after_amount(String gcb_after_amount) {
        this.gcb_after_amount = gcb_after_amount;
    }

    public String getCc_safety_arrears_num() {
        return cc_safety_arrears_num;
    }

    public void setCc_safety_arrears_num(String cc_safety_arrears_num) {
        this.cc_safety_arrears_num = cc_safety_arrears_num;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
