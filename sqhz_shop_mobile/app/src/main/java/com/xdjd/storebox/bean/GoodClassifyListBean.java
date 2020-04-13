package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by lijipei on 2016/11/24.
 */

public class GoodClassifyListBean extends BaseBean{
    private List<GoodClassifyListBean> listData;

    private String gcId; //一级分类id
    private String gcCode;
    private String gcName; //一级分类名称
    private String backType;//	类型	1标签 2 分类3全部
    private List<GoodClassifyListBean> subList; //二级分类集合

    public String getGcCode() {
        return gcCode;
    }

    public void setGcCode(String gcCode) {
        this.gcCode = gcCode;
    }

    public List<GoodClassifyListBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodClassifyListBean> listData) {
        this.listData = listData;
    }

    public String getBackType() {
        return backType;
    }

    public void setBackType(String backType) {
        this.backType = backType;
    }

    public List<GoodClassifyListBean> getSubList() {
        return subList;
    }

    public void setSubList(List<GoodClassifyListBean> subList) {
        this.subList = subList;
    }

    public String getGcId() {
        return gcId;
    }

    public void setGcId(String gcId) {
        this.gcId = gcId;
    }

    public String getGcName() {
        return gcName;
    }

    public void setGcName(String gcName) {
        this.gcName = gcName;
    }
}
