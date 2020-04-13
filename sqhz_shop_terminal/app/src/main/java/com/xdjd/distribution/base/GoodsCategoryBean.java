package com.xdjd.distribution.base;

import com.xdjd.distribution.bean.BaseBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/11
 *     desc   : 商品一二级分类bean
 *     version: 1.0
 * </pre>
 */

public class GoodsCategoryBean extends BaseBean {

    private List<GoodsCategoryBean> listData;//	仓库列表

    private String gc_id;//	仓库id
    private String gc_code = "";//	仓库编码
    private String gc_name = "";//	仓库名称
    private String type = "";//	类型	Type为3时 返回的日期,4重点  5推荐 6我常买 7促销

    private List<GoodsCategoryBean> secondCategoryList;//	二级分类列表

    //-------还货参数-------
    private String oa_applycode;//	申请单编号
    private String oa_remarks;//	备注
    private String oa_applydate;//	日期
    private String oa_id;//	    主键

    public String getOa_applycode() {
        return oa_applycode;
    }

    public void setOa_applycode(String oa_applycode) {
        this.oa_applycode = oa_applycode;
    }

    public String getOa_remarks() {
        return oa_remarks;
    }

    public void setOa_remarks(String oa_remarks) {
        this.oa_remarks = oa_remarks;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GoodsCategoryBean> getListData() {
        return listData;
    }

    public void setListData(List<GoodsCategoryBean> listData) {
        this.listData = listData;
    }

    public String getGc_id() {
        return gc_id;
    }

    public void setGc_id(String gc_id) {
        this.gc_id = gc_id;
    }

    public String getGc_code() {
        return gc_code;
    }

    public void setGc_code(String gc_code) {
        this.gc_code = gc_code;
    }

    public String getGc_name() {
        return gc_name;
    }

    public void setGc_name(String gc_name) {
        this.gc_name = gc_name;
    }

    public List<GoodsCategoryBean> getSecondCategoryList() {
        return secondCategoryList;
    }

    public void setSecondCategoryList(List<GoodsCategoryBean> secondCategoryList) {
        this.secondCategoryList = secondCategoryList;
    }
}
