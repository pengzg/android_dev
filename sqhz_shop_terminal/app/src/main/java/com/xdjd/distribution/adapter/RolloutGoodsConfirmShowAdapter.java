package com.xdjd.distribution.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.view.AnimatedExpandableListView;

import java.math.BigDecimal;
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

public class RolloutGoodsConfirmShowAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

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
                    inflate(R.layout.item_rollout_goods_confirm_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText((i+1)+"");
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
                    inflate(R.layout.item_rollout_goods_confirm_child, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }
        PHOrderDetailBean bean = list.get(i).getListGoodsData().get(i1);
        holder.mTvGoodName.setText(bean.getGoodsName());//商品名称

        BigDecimal maxPrice;
        BigDecimal minPrice;
        if ("1".equals(bean.getUnitNum())) {
            if (TextUtils.isEmpty(bean.getMinPrice())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(bean.getMinPrice());
            }
            holder.mTvPriceUnitNameref.setText(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + bean.getMinName());
            holder.mTvGoodsNumNameref.setText(bean.getMinNum() + bean.getMinName());
        } else {
            if (TextUtils.isEmpty(bean.getMaxPrice())) {
                maxPrice = BigDecimal.ZERO;
            } else {
                maxPrice = new BigDecimal(bean.getMaxPrice());
            }

            if (TextUtils.isEmpty(bean.getMinPrice())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(bean.getMinPrice());
            }

            holder.mTvPriceUnitNameref.setText(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + bean.getMaxName()
                    + minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + bean.getMinName());

            holder.mTvGoodsNumNameref.setText(bean.getMaxNum() + bean.getMaxName() +
                    bean.getMinNum() + bean.getMinName());
        }

        holder.mTvTotalPrice.setText("¥:" + bean.getTotalPrice());
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


    class ViewHolder {
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


    class ViewHolderChild {
        @BindView(R.id.tv_good_name)
        TextView mTvGoodName;
        @BindView(R.id.tv_price_unit_nameref)
        TextView mTvPriceUnitNameref;
        @BindView(R.id.tv_goods_num_nameref)
        TextView mTvGoodsNumNameref;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
