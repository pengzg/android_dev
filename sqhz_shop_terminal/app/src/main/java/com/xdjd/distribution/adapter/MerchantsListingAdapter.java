package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ShopListingBean;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MerchantsListingAdapter extends BaseAdapter {

    List<ShopListingBean> list;

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_merchants_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (!"".equals(list.get(i).getMs_img()) && null != list.get(i).getMs_img()) {
            Glide.with(viewGroup.getContext()).load(list.get(i).getMs_img()).
                    error(R.mipmap.customer_img).into(holder.mCsImage);
        }

        holder.mTvName.setText(list.get(i).getMs_name());
        holder.mTvMobile.setText(list.get(i).getMs_mobile());
        //        holder.mTvContactsName.setText(list.get(i).get);
        holder.mTvAddress.setText(list.get(i).getMs_address());

        return view;
    }

    public void setData(List<ShopListingBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_contacts_name)
        TextView mTvContactsName;
        @BindView(R.id.tv_mobile)
        TextView mTvMobile;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.cs_image)
        CircleImageView mCsImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
