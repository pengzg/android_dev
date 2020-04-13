package com.xdjd.storebox.bean;

import java.util.List;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PromoteRankBean extends BaseBean {
    private List<PromoteRankBean> dataList;
    private String spread;//推广员姓名
    private String shopnum;//推广人数
    private String num;//订单总数
    private String settAmount;//订单金额

    public List<PromoteRankBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<PromoteRankBean> dataList) {
        this.dataList = dataList;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }

    public String getShopnum() {
        return shopnum;
    }

    public void setShopnum(String shopnum) {
        this.shopnum = shopnum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(String settAmount) {
        this.settAmount = settAmount;
    }
}
