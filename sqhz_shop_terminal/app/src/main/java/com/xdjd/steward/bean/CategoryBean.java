package com.xdjd.steward.bean;

import com.xdjd.distribution.bean.BaseBean;

/**
 * Created by lijipei on 2017/10/26.
 */

public class CategoryBean extends BaseBean {

    private int categoryId;
    private String categoryName;

    public CategoryBean(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
