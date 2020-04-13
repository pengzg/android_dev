package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/7
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class BrandBean extends BaseBean {

    private String billCode;//收款单号code

    private List<BrandBean> listData;//	品牌列表

    private String bd_id;//	品牌id
    private String bd_code;//	品牌编码
    private String bd_title;//	品牌名称

    //品牌查询欠款字段
    private String debtBalance;//	欠款金额

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getDebtBalance() {
        return debtBalance;
    }

    public void setDebtBalance(String debtBalance) {
        this.debtBalance = debtBalance;
    }

    public List<BrandBean> getListData() {
        return listData;
    }

    public void setListData(List<BrandBean> listData) {
        this.listData = listData;
    }

    public String getBd_id() {
        return bd_id;
    }

    public void setBd_id(String bd_id) {
        this.bd_id = bd_id;
    }

    public String getBd_code() {
        return bd_code;
    }

    public void setBd_code(String bd_code) {
        this.bd_code = bd_code;
    }

    public String getBd_title() {
        return bd_title;
    }

    public void setBd_title(String bd_title) {
        this.bd_title = bd_title;
    }
}
