package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PHGoodsDetailListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsGoodsAdapter extends BaseAdapter {
    List<PHGoodsDetailListBean> list;

    public void setData(List<PHGoodsDetailListBean> list) {
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
                    inflate(R.layout.item_rollout_goods_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText((i + 1) + "");
        holder.tvGoodName.setText(list.get(i).getGoodsName());
        holder.tvTotalNum.setText("已铺总数量：" + list.get(i).getPhTotalNum_desc());

        if ("".equals(list.get(i).getRefundNum_desc()) || list.get(i).getRefundNum_desc() == null) {
            holder.mTvRecallnumDesc.setText("已撤货：" + "0");
        } else {
            holder.mTvRecallnumDesc.setText("已撤货：" + list.get(i).getRefundNum_desc());
        }
        if ("".equals(list.get(i).getPhSaleNum_desc()) || list.get(i).getPhSaleNum_desc() == null) {
            holder.tvSaleNum.setText("已销售：" + "0");
        } else {
            holder.tvSaleNum.setText("已销售：" + list.get(i).getPhSaleNum_desc());
        }
        if ("".equals(list.get(i).getResidueNum_desc()) || list.get(i).getResidueNum_desc() == null) {
            holder.tvSyNum.setText("未销售：" + "0");
        } else {
            holder.tvSyNum.setText("未销售：" + list.get(i).getResidueNum_desc());
        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_total_num)
        TextView tvTotalNum;
        @BindView(R.id.tv_sale_num)
        TextView tvSaleNum;
        @BindView(R.id.tv_sy_num)
        TextView tvSyNum;
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_recallnum_desc)
        TextView mTvRecallnumDesc;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
