package com.bikejoy.testdemo.recycleradapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bikejoy.testdemo.R;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/3/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerListingAdapter extends BaseQuickAdapter<String,BaseViewHolder>{


    public CustomerListingAdapter(List<String> data) {
        super(R.layout.item_member_information,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,"李沛");
        helper.setText(R.id.tv_address,"北京市海淀区中关村南大街12号");
        helper.setText(R.id.tv_mobile,"13683310026");
    }
}
