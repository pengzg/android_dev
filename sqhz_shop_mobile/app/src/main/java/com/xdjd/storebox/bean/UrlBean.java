package com.xdjd.storebox.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/1
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UrlBean extends BaseBean {

    private int id;

    private String ip;
    private String domain_name;
    private String is_select;//0是未使用,1是使用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIs_select() {
        return is_select;
    }

    public void setIs_select(String is_select) {
        this.is_select = is_select;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDomain_name() {
        return domain_name;
    }

    public void setDomain_name(String domain_name) {
        this.domain_name = domain_name;
    }
}
