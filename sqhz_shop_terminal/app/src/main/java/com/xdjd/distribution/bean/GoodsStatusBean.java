package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsStatusBean extends BaseBean {

    private List<GoodsStatusBean> listData;//	List对象	如果有值则在APP上提示

    private int code;//	Code	商品状态 1 正 2 临 3残 4过
    private String name;//	名称

    public List<GoodsStatusBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodsStatusBean> listData) {
        this.listData = listData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
