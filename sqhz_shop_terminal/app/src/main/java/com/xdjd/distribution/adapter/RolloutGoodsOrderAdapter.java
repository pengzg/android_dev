package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.view.AnimatedExpandableListView;

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

public class RolloutGoodsOrderAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    public void setData(List<PHOrderDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private List<PHOrderDetailBean> list;

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i).getListGoodsData() == null ? 0 : list.get(i).getListGoodsData().size();
    }


    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getListGoodsData().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    //获得子列表项的Id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_rollout_goods_order_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText((i + 1) + "");
        holder.tvOrderCode.setText(list.get(i).getOrderCode());//订单号
        holder.tvTotalAmount.setText("总金额：" + list.get(i).getTotalAmount());//总金额
        holder.tvAddTime.setText("下单时间：" + list.get(i).getAddTime());//下单时间
        return view;
    }

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_rollout_goods_order_child, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }
        PHOrderDetailBean bean = list.get(i).getListGoodsData().get(i1);

        holder.tvGoodName.setText(bean.getGoodsName());//商品名称
        holder.tvMaxpriceDesc.setText(bean.getMaxPrice() + "/" + bean.getMaxName());//大单位价格描述
        holder.tvMinpriceDesc.setText(bean.getMinPrice() + "/" + bean.getMinName());//小单位价格描述
        holder.tvTotalnumDesc.setText("总数量：" + bean.getPhTotalNum_desc());//铺货总数量描述

        if ("".equals(bean.getRefundNum_desc()) || bean.getRefundNum_desc() == null) {
            holder.mTvRecallnumDesc.setText("已撤货：" + "0");//撤货数量描述
        } else {
            holder.mTvRecallnumDesc.setText("已撤货:"+bean.getRefundNum_desc());//撤货数量描述
        }
        if ("".equals(bean.getPhSaleNum_desc()) || bean.getPhSaleNum_desc() == null) {
            holder.tvSalenumDesc.setText("已销售：" + "0");//已销售数量描述
        } else {
            holder.tvSalenumDesc.setText("已销售：" + bean.getPhSaleNum_desc());//已销售数量描述
        }
        if ("".equals(bean.getResidueNum_desc()) || bean.getResidueNum_desc() == null) {
            holder.tvSynumDesc.setText("未销售：" + "0");//剩余数量描述
        } else {
            holder.tvSynumDesc.setText("未销售：" + bean.getResidueNum_desc());//剩余数量描述
        }
        holder.tvSygoodsAmount.setText(bean.getSyGoodsAmount());//剩余商品金额
        return view;
    }


    //获得子列表项，比较重要、难理解的 一个方法
    //    @Override
    //    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
    //
    //    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    static class ViewHolder {
        @BindView(R.id.tv_order_code)
        TextView tvOrderCode;
        @BindView(R.id.tv_total_amount)
        TextView tvTotalAmount;
        @BindView(R.id.tv_add_time)
        TextView tvAddTime;
        @BindView(R.id.tv_index)
        TextView mTvIndex;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderChild {
        @BindView(R.id.tv_good_name)
        TextView tvGoodName;
        @BindView(R.id.tv_maxprice_desc)
        TextView tvMaxpriceDesc;
        @BindView(R.id.tv_minprice_desc)
        TextView tvMinpriceDesc;
        @BindView(R.id.tv_totalnum_desc)
        TextView tvTotalnumDesc;
        @BindView(R.id.tv_salenum_desc)
        TextView tvSalenumDesc;
        @BindView(R.id.tv_synum_desc)
        TextView tvSynumDesc;
        @BindView(R.id.tv_sygoods_amount)
        TextView tvSygoodsAmount;
        @BindView(R.id.tv_recallnum_desc)
        TextView mTvRecallnumDesc;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
