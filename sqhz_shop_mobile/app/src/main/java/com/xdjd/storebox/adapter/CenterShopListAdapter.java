package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.CenterShopListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/3/13.
 */

public class CenterShopListAdapter extends BaseAdapter {

    private List<CenterShopListBean> list;

    public void setData(List<CenterShopListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_center_shop_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mNameTv.setText(list.get(position).getMs_name());
        holder.mDistanceTv.setText(list.get(position).getDistance()+"km");
        holder.mAddressTv.setText(list.get(position).getMs_address());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.distance_tv)
        TextView mDistanceTv;
        @BindView(R.id.address_tv)
        TextView mAddressTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
