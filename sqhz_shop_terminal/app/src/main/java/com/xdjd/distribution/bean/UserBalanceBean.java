package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/7
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserBalanceBean extends BaseBean {
    private String name;//	编码
    private String saftBalance;//	安全欠款	空为不限制
    private String balance;//	余额

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSaftBalance() {
        return saftBalance;
    }

    public void setSaftBalance(String saftBalance) {
        this.saftBalance = saftBalance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
