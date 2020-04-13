package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ReceiptPaymentBean;
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

public class ReceiptPaymentQueryAdapter extends BaseAdapter {

    public List<ReceiptPaymentBean> list;


    private ItemOnListener listener;

    public ReceiptPaymentQueryAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<ReceiptPaymentBean> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_receipt_payment_query, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i+1)+"");
        holder.mTvCustomerName.setText(list.get(i).getGc_customerid_nameref());
        holder.mTvAmount.setText("金额:¥" + list.get(i).getGcrr_total_amount());
        if (list.get(i).getGcrr_discount_amount() == null || "".equals(list.get(i).getGcrr_discount_amount())) {
            holder.mTvPreferentialPrice.setText("优惠:¥0.00");
        } else {
            holder.mTvPreferentialPrice.setText("优惠:¥" + list.get(i).getGcrr_discount_amount());
        }

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
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_amount)
        TextView mTvAmount;
        @BindView(R.id.tv_preferential_price)
        TextView mTvPreferentialPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
