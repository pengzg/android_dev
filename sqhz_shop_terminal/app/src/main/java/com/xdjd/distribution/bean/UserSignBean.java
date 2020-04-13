package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserSignBean extends BaseBean {

    private String cud_id;//	签到id	 如果签到则返回 缓存到本地

    public String getCud_id() {
        return cud_id;
    }

    public void setCud_id(String cud_id) {
        this.cud_id = cud_id;
    }
}
