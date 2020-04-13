package com.xdjd.steward.adapter;

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
 *     time   : 2018/1/2
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsSummaryAdapter extends BaseAdapter {
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
                    inflate(R.layout.item_rollout_goods_summary, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PHGoodsDetailListBean bean = list.get(i);
        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvGoodName.setText(bean.getGoodsName());
        holder.mTvShopNum.setText("已铺货店铺数量:"+bean.getPhShopNum()+"家");

        holder.mTvTotalNum.setText("已铺总数量：" + bean.getPhTotalNum_desc());

        if ("".equals(bean.getRefundNum_desc()) || bean.getRefundNum_desc() == null) {
            holder.mTvRecallNum.setText("已撤货：" + "0");
        } else {
            holder.mTvRecallNum.setText("已撤货：" + bean.getRefundNum_desc());
        }
        if ("".equals(bean.getPhSaleNum_desc()) || bean.getPhSaleNum_desc() == null) {
            holder.mTvSaleNum.setText("已销售：" + "0");
        } else {
            holder.mTvSaleNum.setText("已销售：" + bean.getPhSaleNum_desc());
        }
        if ("".equals(bean.getResidueNum_desc()) || bean.getResidueNum_desc() == null) {
            holder.mTvNosaleNum.setText("未销售：" + "0");
        } else {
            holder.mTvNosaleNum.setText("未销售：" + bean.getResidueNum_desc());
        }

        return view;
    }


    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_good_name)
        TextView mTvGoodName;
        @BindView(R.id.tv_shop_num)
        TextView mTvShopNum;
        @BindView(R.id.tv_total_num)
        TextView mTvTotalNum;
        @BindView(R.id.tv_recall_num)
        TextView mTvRecallNum;
        @BindView(R.id.tv_sale_num)
        TextView mTvSaleNum;
        @BindView(R.id.tv_nosale_num)
        TextView mTvNosaleNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
