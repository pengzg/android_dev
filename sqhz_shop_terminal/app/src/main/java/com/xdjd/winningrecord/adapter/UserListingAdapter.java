package com.xdjd.winningrecord.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.MemberBean;

import java.util.List;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/6
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UserListingAdapter  extends BaseQuickAdapter<MemberBean, BaseViewHolder> {

    private Context mContext;

    public void setContext(Context context) {
        mContext = context;
    }

    public UserListingAdapter(List<MemberBean> data) {
        super(R.layout.item_user_listing, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberBean item) {
        Glide.with(mContext).load(item.getMb_img()).error(R.mipmap.customer_img).
                into((ImageView) helper.getView(R.id.cs_image));
        helper.setText(R.id.tv_name,item.getMb_nickname());
        helper.setText(R.id.tv_address,item.getMb_city());
        helper.setText(R.id.tv_mobile,item.getMb_mobile());
    }
}
