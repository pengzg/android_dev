package com.xdjd.storebox.bean;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/3/30
 *     desc   : 全部和我的模块列表bean
 *     version: 1.0
 * </pre>
 */

public class GridItemBean extends BaseBean {

    private String id;
    private String name;
    private int imgId;  //图片id


    public GridItemBean(String id, String name, int imgId) {
        this.id = id;
        this.name = name;
        this.imgId = imgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
