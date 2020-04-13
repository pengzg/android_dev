package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.RolloutGoodsOrderBean;

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
    private int type;

    public RolloutGoodsOrderListAdapter(List<RolloutGoodsOrderBean> list, int type) {
        this.list = list;
        this.type = type;
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
        RolloutGoodsOrderBean bean = list.get(i);
        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvOrderCode.setText(bean.getOrderCode());
        holder.mTvCustomerName.setText(bean.getShopName());

        switch (type) {
            case 1:
//                if ("12".equals(bean.getOrderStatus())) {
//                    holder.mTvOrderState.setText("已完成");
//                } else {
//                    holder.mTvOrderState.setText("未完成");
//                }
                holder.mTvOrderState.setText(list.get(i).getOrderStatus_nameref());
                holder.mTvOrderTitle.setText("铺货金额");
                holder.mTvOrderAmount.setText("¥" + bean.getTotalAmount());
                holder.mLlOrderAmount.setVisibility(View.VISIBLE);
                holder.mTvOrderTimeTitle.setText("铺货时间");
                break;
            case 2:
                if ("6".equals(bean.getOrderStatus())) {
                    holder.mTvOrderState.setText("已完成");
                } else {
                    holder.mTvOrderState.setText("已取消");
                }
                holder.mTvOrderTitle.setText("销售金额");
                holder.mTvOrderAmount.setText("¥" + bean.getTotalAmount());
                holder.mLlOrderAmount.setVisibility(View.VISIBLE);
                holder.mTvOrderTimeTitle.setText("销售时间");
                break;
            case 3:
                holder.mLlOrderAmount.setVisibility(View.GONE);
                holder.mTvOrderTimeTitle.setText("撤货时间");
                break;
            case 4:
                holder.mTvOrderState.setText(list.get(i).getOrderStatus_nameref());
                holder.mTvOrderTitle.setText("铺货申报金额");
                holder.mTvOrderAmount.setText("¥" + bean.getTotalAmount());
                holder.mLlOrderAmount.setVisibility(View.VISIBLE);
                holder.mTvOrderTimeTitle.setText("申报时间");
                break;
        }
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
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.tv_create_order_time)
        TextView mTvCreateOrderTime;
        @BindView(R.id.ll_order_amount)
        LinearLayout mLlOrderAmount;
        @BindView(R.id.tv_order_title)
        TextView mTvOrderTitle;
        @BindView(R.id.tv_order_time_title)
        TextView mTvOrderTimeTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
