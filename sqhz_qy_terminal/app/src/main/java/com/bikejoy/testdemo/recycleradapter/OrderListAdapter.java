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

public class OrderListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OrderListAdapter(List<String> data) {
        super(R.layout.item_order_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.getView(R.id.rv_goods_list);

    }

//    static class ViewHolder {
//        @BindView(R.id.client_img)
//        RoundedImageView mClientImg;
//        @BindView(R.id.rv_goods_list)
//        RecyclerView mRvGoodsList;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
}
