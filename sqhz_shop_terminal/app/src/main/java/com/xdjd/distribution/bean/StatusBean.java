package com.xdjd.distribution.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/1.
 * 公共的状态bean
 */

public class StatusBean implements Serializable {

    private int type;//状态
    private String typeName;//状态名称

    public StatusBean(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
