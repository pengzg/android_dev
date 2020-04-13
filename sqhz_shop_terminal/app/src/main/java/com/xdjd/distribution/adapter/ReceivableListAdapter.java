package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ReceivableListBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ReceivableListAdapter extends BaseAdapter {

    List<ReceivableListBean> list;
    ItemOnListener listener;

    public ReceivableListAdapter(ItemOnListener listener) {
        this.listener = listener;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_customer_yks_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ReceivableListBean bean = list.get(i);

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvCustomerName.setText(bean.getGr_customerid_nameref());
        holder.mTvTotalAmount.setText("总金额:¥" + bean.getGr_total_amount());
        holder.mTvTradeAmount.setText("已结算:¥" + bean.getGr_trade_amount());
        holder.mTvWsAmount.setText("未收:¥" + bean.getWs_amount());
        holder.mTvDiscountsAmount.setText("优惠:¥"+bean.getGr_discount_amount());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onItem(i);
                }
            }
        });

        return view;
    }

    public void setData(List<ReceivableListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_trade_amount)
        TextView mTvTradeAmount;
        @BindView(R.id.tv_ws_amount)
        TextView mTvWsAmount;
        @BindView(R.id.tv_discounts_amount)
        TextView mTvDiscountsAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
