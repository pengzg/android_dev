package com.xdjd.distribution.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/15
 *     desc   : 订货、代销参数组合提交bean
 *     version: 1.0
 * </pre>
 */

public class OrderApplyDetailBean extends BaseBean {

    private String oad_goods_id;//			商品id
    private String oad_goods_price_id;//			商品价格id
    private String oad_pricetype;//			销售类型
    private String oad_goods_num_min;//			小单位数量
    private String oad_goods_num_max;//			大单位数量
    private String oad_price_min;//			小单位价格
    private String oad_price_max;//			大单位价格
    private String oad_price_strategyid;//			价格方案id

    public String getOad_goods_id() {
        return oad_goods_id;
    }

    public void setOad_goods_id(String oad_goods_id) {
        this.oad_goods_id = oad_goods_id;
    }

    public String getOad_goods_price_id() {
        return oad_goods_price_id;
    }

    public void setOad_goods_price_id(String oad_goods_price_id) {
        this.oad_goods_price_id = oad_goods_price_id;
    }

    public String getOad_pricetype() {
        return oad_pricetype;
    }

    public void setOad_pricetype(String oad_pricetype) {
        this.oad_pricetype = oad_pricetype;
    }

    public String getOad_goods_num_min() {
        return oad_goods_num_min;
    }

    public void setOad_goods_num_min(String oad_goods_num_min) {
        this.oad_goods_num_min = oad_goods_num_min;
    }

    public String getOad_goods_num_max() {
        return oad_goods_num_max;
    }

    public void setOad_goods_num_max(String oad_goods_num_max) {
        this.oad_goods_num_max = oad_goods_num_max;
    }

    public String getOad_price_min() {
        return oad_price_min;
    }

    public void setOad_price_min(String oad_price_min) {
        this.oad_price_min = oad_price_min;
    }

    public String getOad_price_max() {
        return oad_price_max;
    }

    public void setOad_price_max(String oad_price_max) {
        this.oad_price_max = oad_price_max;
    }

    public String getOad_price_strategyid() {
        return oad_price_strategyid;
    }

    public void setOad_price_strategyid(String oad_price_strategyid) {
        this.oad_price_strategyid = oad_price_strategyid;
    }
}
