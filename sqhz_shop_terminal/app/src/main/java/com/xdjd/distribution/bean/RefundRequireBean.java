package com.xdjd.distribution.bean;

/**
 * Created by lijipei on 2017/6/26.
 */

public class RefundRequireBean extends BaseBean{

    private String eim_id;//	主键
    private String eim_code;//	编码

    public String getEim_id() {
        return eim_id;
    }

    public void setEim_id(String eim_id) {
        this.eim_id = eim_id;
    }

    public String getEim_code() {
        return eim_code;
    }

    public void setEim_code(String eim_code) {
        this.eim_code = eim_code;
    }
}
