package com.xdjd.distribution.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijipei on 2017/8/10.
 */

public class InventoryPhotoEvent {

    // 上传后返回的图片路径
    private ArrayList<String> pathList;
    // 图片sd地址
    private List<String> drr;

    public InventoryPhotoEvent(ArrayList<String> pathList, List<String> drr) {
        this.pathList = pathList;
        this.drr = drr;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public List<String> getDrr() {
        return drr;
    }

}
