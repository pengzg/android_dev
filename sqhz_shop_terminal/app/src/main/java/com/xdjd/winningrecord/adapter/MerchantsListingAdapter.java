package com.xdjd.winningrecord.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddressListBean;

import java.util.List;

/**
 * Created by lijipei on 2017/10/26.
 */

public class MerchantsListingAdapter extends BaseQuickAdapter<AddressListBean, BaseViewHolder> {


    public MerchantsListingAdapter(List<AddressListBean> data) {
        super(R.layout.item_mearchants_listing, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressListBean item) {
        helper.setText(R.id.tv_name,item.getCc_name());
        helper.setText(R.id.tv_contacts_name,item.getCc_contacts_name());
        helper.setText(R.id.tv_address,item.getCc_address());
        helper.setText(R.id.tv_mobile,item.getCc_contacts_mobile());
    }
}
