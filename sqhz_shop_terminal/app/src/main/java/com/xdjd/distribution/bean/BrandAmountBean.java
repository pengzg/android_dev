package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/5
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BrandAmountBean extends BaseBean {
    private List<BrandAmountBean> listData;//	订单商品列表
    private String brandid;//	品牌id
    private String brandname;//	品牌名称
    private String totalamount;//	品牌发货金额
    private String yeamount;//品牌余额字段

    private String bd_is_main;//	是否是综合品牌	Y是 N不是

    //------接口上传参数------
    private String deliveryAmount;//	发货金额
    private String YEAmount;//	余额--使用余额
    private String YSAmount;//	应收金额

    public String getBd_is_main() {
        return bd_is_main;
    }

    public void setBd_is_main(String bd_is_main) {
        this.bd_is_main = bd_is_main;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public String getYEAmount() {
        return YEAmount;
    }

    public void setYEAmount(String YEAmount) {
        this.YEAmount = YEAmount;
    }

    public String getYSAmount() {
        return YSAmount;
    }

    public void setYSAmount(String YSAmount) {
        this.YSAmount = YSAmount;
    }

    public String getYeamount() {
        return yeamount;
    }

    public void setYeamount(String yeamount) {
        this.yeamount = yeamount;
    }

    public List<BrandAmountBean> getListData() {
        return listData;
    }

    public void setListData(List<BrandAmountBean> listData) {
        this.listData = listData;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
