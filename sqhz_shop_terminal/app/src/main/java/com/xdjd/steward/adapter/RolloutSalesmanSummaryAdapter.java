package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PhSalesmanBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/1/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutSalesmanSummaryAdapter extends BaseAdapter {

    List<PhSalesmanBean> list;
    ItemOnListener listener;

    public void setListener(ItemOnListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_rollout_salesman_summary, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PhSalesmanBean bean = list.get(i);

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(bean.getSalesid_nameref());
        holder.mTvPhTotalNum.setText("铺货店铺数量:"+bean.getPhShopNum());

        holder.mTvTotalAmount.setText("总金额:¥" + bean.getTotalAmount());

        if ("".equals(bean.getRefundAmount()) || bean.getRefundAmount() == null) {
            holder.mTvRecallAmount.setText("已撤货:¥0");
        } else {
            holder.mTvRecallAmount.setText("已撤货:¥" + bean.getRefundAmount());
        }
        if ("".equals(bean.getSaleAmount()) || bean.getSaleAmount() == null) {
            holder.mTvSaleAmount.setText("已销售:¥0");
        } else {
            holder.mTvSaleAmount.setText("已销售:¥" + bean.getSaleAmount());
        }
        if ("".equals(bean.getSyGoodsAmount()) || bean.getSyGoodsAmount() == null) {
            holder.mTvSyAmount.setText("未销售:" + "¥0");
        } else {
            holder.mTvSyAmount.setText("未销售:¥" + bean.getSyGoodsAmount());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    public void setData(List<PhSalesmanBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_ph_total_num)
        TextView mTvPhTotalNum;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_recall_amount)
        TextView mTvRecallAmount;
        @BindView(R.id.tv_sale_amount)
        TextView mTvSaleAmount;
        @BindView(R.id.tv_sy_amount)
        TextView mTvSyAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
