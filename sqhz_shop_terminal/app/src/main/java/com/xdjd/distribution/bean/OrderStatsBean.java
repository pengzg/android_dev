package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderStatsBean extends BaseBean {

    private List<OrderStatsBean> dataList;//	商品列表

    private String bd_name;//	名字
    private String bd_code;//	编码

    public List<OrderStatsBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderStatsBean> dataList) {
        this.dataList = dataList;
    }

    public String getBd_name() {
        return bd_name;
    }

    public void setBd_name(String bd_name) {
        this.bd_name = bd_name;
    }

    public String getBd_code() {
        return bd_code;
    }

    public void setBd_code(String bd_code) {
        this.bd_code = bd_code;
    }
}
