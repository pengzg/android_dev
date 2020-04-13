package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StorehouseBean extends BaseBean {

    private List<StorehouseBean> listData;//	仓库列表

    private String bs_id;//	仓库id
    private String bs_code;//	仓库编码
    private String bs_name = "";//	仓库名称

    public List<StorehouseBean> getListData() {
        return listData;
    }

    public void setListData(List<StorehouseBean> listData) {
        this.listData = listData;
    }

    public String getBs_id() {
        return bs_id;
    }

    public void setBs_id(String bs_id) {
        this.bs_id = bs_id;
    }

    public String getBs_code() {
        return bs_code;
    }

    public void setBs_code(String bs_code) {
        this.bs_code = bs_code;
    }

    public String getBs_name() {
        return bs_name;
    }

    public void setBs_name(String bs_name) {
        this.bs_name = bs_name;
    }
}
