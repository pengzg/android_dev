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

public class RolloutGoodsShopAdapter extends BaseAdapter {

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
                    inflate(R.layout.item_rollout_goods_shop_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        PhShopHzBean bean = list.get(i);
        holder.mTvIndex.setText((i + 1) + "");

        holder.mTvShopName.setText(bean.getShopName());
        holder.mTvLastDate.setText(bean.getLastPhDate());
        holder.mTvGoodsName.setText(bean.getGoodsName());

        holder.mTvTotalNum.setText("已铺总数量：" + bean.getPhTotalNum_desc());

        if ("".equals(bean.getRefundNum_desc()) || bean.getRefundNum_desc() == null) {
            holder.mTvRecallnumDesc.setText("已撤货：" + "0");
        } else {
            holder.mTvRecallnumDesc.setText("已撤货：" + bean.getRefundNum_desc());
        }
        if ("".equals(bean.getPhSaleNum_desc()) || bean.getPhSaleNum_desc() == null) {
            holder.mTvSaleNum.setText("已销售：" + "0");
        } else {
            holder.mTvSaleNum.setText("已销售：" + bean.getPhSaleNum_desc());
        }
        if ("".equals(bean.getResidueNum_desc()) || bean.getResidueNum_desc() == null) {
            holder.mTvSyNum.setText("未销售：" + "0");
        } else {
            holder.mTvSyNum.setText("未销售：" + bean.getResidueNum_desc());
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
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_total_num)
        TextView mTvTotalNum;
        @BindView(R.id.tv_recallnum_desc)
        TextView mTvRecallnumDesc;
        @BindView(R.id.tv_sale_num)
        TextView mTvSaleNum;
        @BindView(R.id.tv_sy_num)
        TextView mTvSyNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
