package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PrintListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/5/29.
 */

public class VoucherReprintAdapter extends BaseAdapter {

    List<PrintListBean> list;

    public void setData(List<PrintListBean> list) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_reprint, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText(position + 1 + "");
        holder.mTvCode.setText(list.get(position).getCode());
        holder.mTvName.setText(list.get(position).getCustomerid_nameref());
        if ("2".equals(list.get(position).getType())){
            holder.mTvOrderType.setText("");
            holder.mTvOrderAmount.setText("收款金额: ¥" + list.get(position).getAmount());
            holder.mTvTime.setText("收款日期: "+ list.get(position).getDate());
        }else{
            holder.mTvOrderType.setText(list.get(position).getSource_nameref());
            holder.mTvOrderAmount.setText("订单金额: ¥" + list.get(position).getAmount());
            holder.mTvTime.setText("下单日期: "+ list.get(position).getDate());
        }
        holder.mTvOrderState.setText(list.get(position).getStats_nameref());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_order_type)
        TextView mTvOrderType;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.tv_time)
        TextView mTvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
