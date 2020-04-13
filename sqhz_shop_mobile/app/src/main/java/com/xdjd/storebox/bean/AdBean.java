package com.xdjd.storebox.bean;

import java.io.Serializable;

/**
 * Created by freestyle_hong on 2016/12/27.
 */

public class AdBean extends BaseBean implements Serializable{
    private String ai_cover;//图片地址
    private int ai_type;//跳转类型
    private String ai_type_value;//跳转值
    private String ai_title;//标题

    public String getAi_title() {
        return ai_title;
    }

    public void setAi_title(String ai_title) {
        this.ai_title = ai_title;
    }

    public int getAi_type() {
        return ai_type;
    }

    public void setAi_type(int ai_type) {
        this.ai_type = ai_type;
    }

    public String getAi_cover() {
        return ai_cover;
    }

    public void setAi_cover(String ai_cover) {
        this.ai_cover = ai_cover;
    }



    public String getAi_type_value() {
        return ai_type_value;
    }

    public void setAi_type_value(String ai_type_value) {
        this.ai_type_value = ai_type_value;
    }
}
