package com.xdjd.distribution.bean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class LineClientBean extends BaseBean {

    private String cc_id;
    private String cc_name = "";//	类别名
    private String count = "0";//	客户数
    private String cc_code = "";//	编码
    private String cc_parentid = "";//	父级id,0是父级分类


    private String cc_type = "";//	类型	1附近  2未定位  3类别

    private List<LineClientBean> dataList;//一级列表数据
    private List<LineClientBean> subList;//	子集类--二级列表数据

    public String getCc_type() {
        return cc_type;
    }

    public void setCc_type(String cc_type) {
        this.cc_type = cc_type;
    }

    public List<LineClientBean> getSubList() {
        return subList;
    }

    public void setSubList(List<LineClientBean> subList) {
        this.subList = subList;
    }

    public List<LineClientBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<LineClientBean> dataList) {
        this.dataList = dataList;
    }

    public String getCc_id() {
        return cc_id;
    }

    public void setCc_id(String cc_id) {
        this.cc_id = cc_id;
    }

    public String getCc_name() {
        return cc_name;
    }

    public void setCc_name(String cc_name) {
        this.cc_name = cc_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCc_code() {
        return cc_code;
    }

    public void setCc_code(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getCc_parentid() {
        return cc_parentid;
    }

    public void setCc_parentid(String cc_parentid) {
        this.cc_parentid = cc_parentid;
    }
}
