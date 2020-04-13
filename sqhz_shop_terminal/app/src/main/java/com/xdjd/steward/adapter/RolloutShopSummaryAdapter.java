package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PhShopHzBean;

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

public class RolloutShopSummaryAdapter extends BaseAdapter {
    List<PhShopHzBean> list;

    public void setData(List<PhShopHzBean> list) {
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
                    inflate(R.layout.item_rollout_shop_summary, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PhShopHzBean bean = list.get(i);
        holder.mTvIndex.setText((i + 1) + "");

        holder.mTvShopName.setText(bean.getShopName());
        holder.mTvLastDate.setText(bean.getLastPhDate());
        holder.mTvGoodsNum.setText("已铺货"+bean.getGoodsKindNum()+"种商品");

        holder.mTvTotalAmount.setText("总金额:¥" + bean.getTotalAmount());

        if ("".equals(bean.getRefundAmount()) || bean.getRefundAmount() == null) {
            holder.mTvRecallAmoun.setText("已撤货:¥0");
        } else {
            holder.mTvRecallAmoun.setText("已撤货:¥" + bean.getRefundAmount());
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
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_shop_name)
        TextView mTvShopName;
        @BindView(R.id.tv_last_date)
        TextView mTvLastDate;
        @BindView(R.id.tv_goods_num)
        TextView mTvGoodsNum;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_recall_amoun)
        TextView mTvRecallAmoun;
        @BindView(R.id.tv_sale_amount)
        TextView mTvSaleAmount;
        @BindView(R.id.tv_sy_amount)
        TextView mTvSyAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
