package com.xdjd.storebox.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RecordsBean extends BaseBean {

    private int id;
    private String account = "";
    private String password = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
