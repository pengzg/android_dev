package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/11
 *     desc   : 销售类型
 *     version: 1.0
 * </pre>
 */

public class SaleTypeBean extends BaseBean {

    private List<SaleTypeBean> listData;//	销售类型列表

    private String sp_code;//	id
    private String sp_name;//	销售类型名称
    private String sp_discount;//	折扣

    public List<SaleTypeBean> getListData() {
        return listData;
    }

    public void setListData(List<SaleTypeBean> listData) {
        this.listData = listData;
    }

    public String getSp_discount() {
        return sp_discount;
    }

    public void setSp_discount(String sp_discount) {
        this.sp_discount = sp_discount;
    }

    public String getSp_code() {
        return sp_code;
    }

    public void setSp_code(String sp_code) {
        this.sp_code = sp_code;
    }

    public String getSp_name() {
        return sp_name;
    }

    public void setSp_name(String sp_name) {
        this.sp_name = sp_name;
    }
}
