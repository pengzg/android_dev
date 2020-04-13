package com.xdjd.steward.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ReceivableListBean;

import java.util.List;

/**
 * Created by lijipei on 2017/10/26.
 */

public class RecyclerReceivableListAdapter extends BaseQuickAdapter<ReceivableListBean, BaseViewHolder> {


    public RecyclerReceivableListAdapter(List<ReceivableListBean> data) {
        super(R.layout.item_customer_yks_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReceivableListBean bean) {

        helper.setText(R.id.tv_index, (helper.getPosition() + 1) + "");
        helper.setText(R.id.tv_customer_name, bean.getGr_customerid_nameref());
        helper.setText(R.id.tv_total_amount, "总金额:¥" + bean.getGr_total_amount());
        helper.setText(R.id.tv_trade_amount, "已结算:¥" + bean.getGr_trade_amount());
        helper.setText(R.id.tv_ws_amount, "未收:¥" + bean.getWs_amount());
        helper.setText(R.id.tv_discounts_amount, "优惠:¥" + bean.getGr_discount_amount());
    }
}
