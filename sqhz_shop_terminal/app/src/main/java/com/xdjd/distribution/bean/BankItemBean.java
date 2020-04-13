package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BankItemBean extends BaseBean {

    private String userId;//	用户信息编号
    private List<BankItemBean> list;//刷卡支付类型列表

    private String bi_id;//	主键
    private String bi_name;//	名称
    private String bi_code;//	编码

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<BankItemBean> getList() {
        return list;
    }

    public void setList(List<BankItemBean> list) {
        this.list = list;
    }

    public String getBi_id() {
        return bi_id;
    }

    public void setBi_id(String bi_id) {
        this.bi_id = bi_id;
    }

    public String getBi_name() {
        return bi_name;
    }

    public void setBi_name(String bi_name) {
        this.bi_name = bi_name;
    }

    public String getBi_code() {
        return bi_code;
    }

    public void setBi_code(String bi_code) {
        this.bi_code = bi_code;
    }
}
