package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.RolloutGoodsOrderBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsOrderListAdapter extends BaseAdapter {

    List<RolloutGoodsOrderBean> list;

    public RolloutGoodsOrderListAdapter(List<RolloutGoodsOrderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rollout_goods_order, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText((i + 1) + "");
        RolloutGoodsOrderBean bean = list.get(i);
        holder.mTvOrderCode.setText(bean.getOrderCode());

        if ("12".equals(bean.getOrderStatus())) {
            holder.mTvOrderState.setText("已完成");
        } else {
            holder.mTvOrderState.setText("未完成");
        }
        holder.mTvOrderAmount.setText("¥" + bean.getTotalAmount());
        holder.mLlOrderAmount.setVisibility(View.VISIBLE);

        holder.mTvCreateOrderTime.setText(bean.getAddTime());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.tv_create_order_time)
        TextView mTvCreateOrderTime;
        @BindView(R.id.ll_order_amount)
        LinearLayout mLlOrderAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
