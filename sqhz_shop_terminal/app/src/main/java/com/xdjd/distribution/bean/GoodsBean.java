package com.xdjd.distribution.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class GoodsBean extends BaseBean implements Serializable,Cloneable{

    private List<GoodsBean> listData;//	商品列表

    private String ggp_id;//	商品priceId
    private String ggp_goodsid;//	商品id
    private String ggp_unit_num;//	商品换算关系
    private String ggs_stock;//	商品库存
    private String ggp_minnum;//	起定量
    private String ggp_addnum;//	最小增量
    private String gg_title;//	商品标题
    private String gg_model = "";//	商品规格
    private String gg_goods_code;//	商品code
    private String gg_unit_max_nameref;//	最大单位描述
    private String gg_unit_min_nameref;//	最小单位描述
    private String max_price;//	最大单位价格
    private String min_price;//	最小单位价格
    private String gps_id;//	价格方案id
    private String od_id = "";//	订单明细id

    private String gg_international_code_max = "";// 大单位
    private String gg_international_code_min = "";//小单位

    private String unit_type;// 1 都有 2 小单位 3 大单位

    private String ggp_unit_type = "1";//	单位基准：1 小单位，2 大单位

    private String inventory_date = "";//盘点日期
    private String display_quantity = "";//陈列数量
    private String duitou_quantity = "";//堆头数量

    //返程列参数
    private String eim_id;//	主键
    private String eim_code;//	编码

    //-------还货参数------
    private int order_surplus_num;//		剩余未发货数量
    private float order_surplus_total;//		剩余未发货金额
    private String oad_id;//		订货明细id

    private String order_surplus_num_str;//剩余未发货数量描述字段

    //-------退货申请商品参数-------
    private String ggs_stock_lq = "0";//	商品  临期库存
    private String ggs_stock_cc = "0";//	商品残次库存
    private String ggs_stock_gq = "0";//	商品过期库存
    private int isSelect = 0;//是否选中,0:选中;1未选中;

    private int goodsStatus;//商品类型
    private String source_id;//	L来源id

    //自定义还货订单在商品中的数据
    private String oa_applycode;//	申请单编号
    private String oa_applydate;//	日期
    private String oa_id;//	    主键

    //-------自定义参数-------
    private int order_surplus_max_num;//		剩余未发货大数量
    private int order_surplus_min_num;//		剩余未发货小数量

    //---------------再来一单参数------------------
    private String max_num;//	大单位数量
    private String min_num;//	小单位数量
    private String saletype;//	销售类型
    private String saletype_nameref;//	销售类型名
    private String saletype_discount;//	销售折扣

    //------------自定义参数---------------
    private String index;//选中商品的下标

    private String saleType="";//商品销售类型id
    private String saleTypeName="";//销售类型name
    private String saleTypeDiscount="";//销售类型折扣

    private String maxNum = "0";//商品最大数量
    private String minNum = "0";//商品最小数量

    private String maxPrice = "0.00";//	最大单位价格
    private String minPrice = "0.00";//	最小单位价格

    private String totalPrice = "0"; //总价格

    private String stock_nameref;//库存描述
    private String remarks; //备注信息

    private String pinyin;
    private String isHasCart = "N";//是否在购物车存在

    //---------订单复制参数--------------
    private String om_ischages;//	是否变价
    private String om_storeid;//	出库仓库
    private String om_storeId_name;//	出库仓库名称
    private int om_ordertype;//	订单类型 1 普通 2 处理 3 退货 4 换货 5 还货
    private String om_source = "";//	订单来源	订单来源 1 客户商城下单;2 终端申请;3 车销;4 库管下单;5 后台下单--2，3能复制


    public String getIsHasCart() {
        return isHasCart;
    }

    public void setIsHasCart(String isHasCart) {
        this.isHasCart = isHasCart;
    }

    public String getEim_id() {
        return eim_id;
    }

    public void setEim_id(String eim_id) {
        this.eim_id = eim_id;
    }

    public String getEim_code() {
        return eim_code;
    }

    public void setEim_code(String eim_code) {
        this.eim_code = eim_code;
    }

    public String getOa_applycode() {
        return oa_applycode;
    }

    public void setOa_applycode(String oa_applycode) {
        this.oa_applycode = oa_applycode;
    }

    public String getOa_applydate() {
        return oa_applydate;
    }

    public void setOa_applydate(String oa_applydate) {
        this.oa_applydate = oa_applydate;
    }

    public String getOa_id() {
        return oa_id;
    }

    public void setOa_id(String oa_id) {
        this.oa_id = oa_id;
    }

    public String getOrder_surplus_num_str() {
        return order_surplus_num_str;
    }

    public void setOrder_surplus_num_str(String order_surplus_num_str) {
        this.order_surplus_num_str = order_surplus_num_str;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getGgs_stock_cc() {
        return ggs_stock_cc;
    }

    public void setGgs_stock_cc(String ggs_stock_cc) {
        this.ggs_stock_cc = ggs_stock_cc;
    }

    public String getGgs_stock_gq() {
        return ggs_stock_gq;
    }

    public void setGgs_stock_gq(String ggs_stock_gq) {
        this.ggs_stock_gq = ggs_stock_gq;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public String getGgs_stock_lq() {
        return ggs_stock_lq;
    }

    public void setGgs_stock_lq(String ggs_stock_lq) {
        this.ggs_stock_lq = ggs_stock_lq;
    }

    public int getOrder_surplus_max_num() {
        return order_surplus_max_num;
    }

    public void setOrder_surplus_max_num(int order_surplus_max_num) {
        this.order_surplus_max_num = order_surplus_max_num;
    }

    public int getOrder_surplus_min_num() {
        return order_surplus_min_num;
    }

    public void setOrder_surplus_min_num(int order_surplus_min_num) {
        this.order_surplus_min_num = order_surplus_min_num;
    }

    public int getOrder_surplus_num() {
        return order_surplus_num;
    }

    public void setOrder_surplus_num(int order_surplus_num) {
        this.order_surplus_num = order_surplus_num;
    }

    public float getOrder_surplus_total() {
        return order_surplus_total;
    }

    public void setOrder_surplus_total(float order_surplus_total) {
        this.order_surplus_total = order_surplus_total;
    }

    public String getOad_id() {
        return oad_id;
    }

    public void setOad_id(String oad_id) {
        this.oad_id = oad_id;
    }

    public String getSaletype_discount() {
        return saletype_discount;
    }

    public void setSaletype_discount(String saletype_discount) {
        this.saletype_discount = saletype_discount;
    }

    public String getMax_num() {
        return max_num;
    }

    public void setMax_num(String max_num) {
        this.max_num = max_num;
    }

    public String getMin_num() {
        return min_num;
    }

    public void setMin_num(String min_num) {
        this.min_num = min_num;
    }

    public String getSaletype() {
        return saletype;
    }

    public void setSaletype(String saletype) {
        this.saletype = saletype;
    }

    public String getSaletype_nameref() {
        return saletype_nameref;
    }

    public void setSaletype_nameref(String saletype_nameref) {
        this.saletype_nameref = saletype_nameref;
    }

    public String getOm_ischages() {
        return om_ischages;
    }

    public void setOm_ischages(String om_ischages) {
        this.om_ischages = om_ischages;
    }

    public String getOm_storeid() {
        return om_storeid;
    }

    public void setOm_storeid(String om_storeid) {
        this.om_storeid = om_storeid;
    }

    public String getOm_storeId_name() {
        return om_storeId_name;
    }

    public void setOm_storeId_name(String om_storeId_name) {
        this.om_storeId_name = om_storeId_name;
    }

    public int getOm_ordertype() {
        return om_ordertype;
    }

    public void setOm_ordertype(int om_ordertype) {
        this.om_ordertype = om_ordertype;
    }

    public String getOm_source() {
        return om_source;
    }

    public void setOm_source(String om_source) {
        this.om_source = om_source;
    }

    public String getGgp_unit_type() {
        return ggp_unit_type;
    }

    public void setGgp_unit_type(String ggp_unit_type) {
        this.ggp_unit_type = ggp_unit_type;
    }

    public String getOd_id() {
        return od_id;
    }

    public void setOd_id(String od_id) {
        this.od_id = od_id;
    }

    public String getInventory_date() {
        return inventory_date;
    }

    public void setInventory_date(String inventory_date) {
        this.inventory_date = inventory_date;
    }

    public String getDisplay_quantity() {
        return display_quantity;
    }

    public void setDisplay_quantity(String display_quantity) {
        this.display_quantity = display_quantity;
    }

    public String getDuitou_quantity() {
        return duitou_quantity;
    }

    public void setDuitou_quantity(String duitou_quantity) {
        this.duitou_quantity = duitou_quantity;
    }

    public String getGps_id() {
        return gps_id;
    }

    public void setGps_id(String gps_id) {
        this.gps_id = gps_id;
    }

    public String getGg_international_code_max() {
        return gg_international_code_max;
    }

    public void setGg_international_code_max(String gg_international_code_max) {
        this.gg_international_code_max = gg_international_code_max;
    }

    public String getGg_international_code_min() {
        return gg_international_code_min;
    }

    public void setGg_international_code_min(String gg_international_code_min) {
        this.gg_international_code_min = gg_international_code_min;
    }

    public String getSaleTypeDiscount() {
        return saleTypeDiscount;
    }

    public void setSaleTypeDiscount(String saleTypeDiscount) {
        this.saleTypeDiscount = saleTypeDiscount;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public Object clone() {
        GoodsBean goodsBean = null;
        try{
            goodsBean = (GoodsBean)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return goodsBean;
    }


    public String getSaleTypeName() {
        return saleTypeName;
    }

    public void setSaleTypeName(String saleTypeName) {
        this.saleTypeName = saleTypeName;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(String maxNum) {
        this.maxNum = maxNum;
    }

    public String getMinNum() {
        return minNum;
    }

    public void setMinNum(String minNum) {
        this.minNum = minNum;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStock_nameref() {
        return stock_nameref;
    }

    public void setStock_nameref(String stock_nameref) {
        this.stock_nameref = stock_nameref;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUnit_type() {
        return unit_type;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public List<GoodsBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodsBean> listData) {
        this.listData = listData;
    }

    public String getGgp_id() {
        return ggp_id;
    }

    public void setGgp_id(String ggp_id) {
        this.ggp_id = ggp_id;
    }

    public String getGgp_goodsid() {
        return ggp_goodsid;
    }

    public void setGgp_goodsid(String ggp_goodsid) {
        this.ggp_goodsid = ggp_goodsid;
    }

    public String getGgp_unit_num() {
        return ggp_unit_num;
    }

    public void setGgp_unit_num(String ggp_unit_num) {
        this.ggp_unit_num = ggp_unit_num;
    }

    public String getGgs_stock() {
        return ggs_stock;
    }

    public void setGgs_stock(String ggs_stock) {
        this.ggs_stock = ggs_stock;
    }

    public String getGgp_minnum() {
        return ggp_minnum;
    }

    public void setGgp_minnum(String ggp_minnum) {
        this.ggp_minnum = ggp_minnum;
    }

    public String getGgp_addnum() {
        return ggp_addnum;
    }

    public void setGgp_addnum(String ggp_addnum) {
        this.ggp_addnum = ggp_addnum;
    }

    public String getGg_title() {
        return gg_title;
    }

    public void setGg_title(String gg_title) {
        this.gg_title = gg_title;
    }

    public String getGg_model() {
        return gg_model;
    }

    public void setGg_model(String gg_model) {
        this.gg_model = gg_model;
    }

    public String getGg_goods_code() {
        return gg_goods_code;
    }

    public void setGg_goods_code(String gg_goods_code) {
        this.gg_goods_code = gg_goods_code;
    }

    public String getGg_unit_max_nameref() {
        return gg_unit_max_nameref;
    }

    public void setGg_unit_max_nameref(String gg_unit_max_nameref) {
        this.gg_unit_max_nameref = gg_unit_max_nameref;
    }

    public String getGg_unit_min_nameref() {
        return gg_unit_min_nameref;
    }

    public void setGg_unit_min_nameref(String gg_unit_min_nameref) {
        this.gg_unit_min_nameref = gg_unit_min_nameref;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getMin_price() {
        return min_price;
    }

    public void setMin_price(String min_price) {
        this.min_price = min_price;
    }
}
