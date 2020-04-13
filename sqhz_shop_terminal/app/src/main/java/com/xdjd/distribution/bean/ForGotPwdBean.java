package com.xdjd.distribution.bean;

/**
 * Created by freestyle_hong on 2017/9/26.
 */

public class ForGotPwdBean extends BaseBean {
    private String mobile;
    private String sms_code;
    private String newPwd;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
