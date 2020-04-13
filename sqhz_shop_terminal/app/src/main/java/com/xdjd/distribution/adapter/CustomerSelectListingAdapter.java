package com.xdjd.distribution.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/26.
 */

public class CustomerSelectListingAdapter extends BaseAdapter {

    List<AddressListBean> list;

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_select_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (!"".equals(list.get(i).getCc_image()) && null != list.get(i).getCc_image()) {
            Glide.with(viewGroup.getContext()).load(list.get(i).getCc_image()).error(R.mipmap.customer_img).
                    into(holder.mCsImage);
        }

        holder.mTvName.setText(list.get(i).getCc_name());
        holder.mTvMobile.setText(list.get(i).getCc_contacts_mobile());
        holder.mTvContactsName.setText(list.get(i).getCc_contacts_name());
        holder.mTvAddress.setText(list.get(i).getCc_address());

        return view;
    }

    public void setData(List<AddressListBean> list) {
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
