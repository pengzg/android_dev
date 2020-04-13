package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PlaceAnOrderBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderGoodsSearchAdapter extends BaseAdapter {

    public List<PlaceAnOrderBean> list;

    private ItemOnListener listener;

    public OrderGoodsSearchAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<PlaceAnOrderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public PlaceAnOrderBean getItem(int i) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_goods_search, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvCustomerName.setText(list.get(i).getOa_customername());
        holder.mTvOrderCode.setText(list.get(i).getOa_applycode());
        holder.mTvOrderDate.setText(list.get(i).getOa_applydate());
        holder.mTvTotalAmount.setText("¥"+list.get(i).getOa_sett_amount());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_order_date)
        TextView mTvOrderDate;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
