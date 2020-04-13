package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.DisplayListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DisplayFeeQueryAdapter extends BaseAdapter {

    List<DisplayListBean> list;

    public void setList(List<DisplayListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_display_fee_query, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i+1)+" ");
        holder.mTvCustomerName.setText(list.get(i).getEim_customerid_nameref());
        holder.mTvOrderCode.setText(list.get(i).getEim_code());
        holder.mTvOrderAmount.setText(list.get(i).getEim_totalamount());
        holder.mTvCreateOrderTime.setText(list.get(i).getEim_billdate());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.ll_order_amount)
        LinearLayout mLlOrderAmount;
        @BindView(R.id.tv_create_order_time)
        TextView mTvCreateOrderTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
