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

public class LineBean extends BaseBean {

    private List<LineBean> lineList;//	线路名

    private String bl_id;//	线路id
    private String bl_name;//	线路名


    public List<LineBean> getLineList() {
        return lineList;
    }

    public void setLineList(List<LineBean> lineList) {
        this.lineList = lineList;
    }

    public LineBean() {
    }

    public LineBean(String bl_id, String bl_name) {
        this.bl_id = bl_id;
        this.bl_name = bl_name;
    }

    public String getBl_id() {
        return bl_id;
    }

    public void setBl_id(String bl_id) {
        this.bl_id = bl_id;
    }

    public String getBl_name() {
        return bl_name;
    }

    public void setBl_name(String bl_name) {
        this.bl_name = bl_name;
    }
}
