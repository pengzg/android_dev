package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.steward.bean.CustOrderAmountBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerOrderGoodsAdapter extends BaseAdapter {

    public List<CustOrderAmountBean> list;

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
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_customer_order_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        CustOrderAmountBean bean = list.get(i);

        holder.mTvIndex.setText((i+1)+"");
        holder.mTvName.setText(bean.getCustomerId_nameref());
        holder.mTvOrderNum.setText("订单数量:"+bean.getOrderNum()+"单");
        holder.mTvOrderAmount.setText("订货总金额:¥"+bean.getAmount());

        return view;
    }

    public void setData(List<CustOrderAmountBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_order_num)
        TextView mTvOrderNum;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
