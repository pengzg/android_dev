package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.SaleOrderByPhOrderBean;
import com.xdjd.view.AnimatedExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/12
 *     desc   : 铺货销售/撤货adapter
 *     version: 1.0
 * </pre>
 */

public class RolloutOperationRecordsAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    List<SaleOrderByPhOrderBean> list;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public void setData(List<SaleOrderByPhOrderBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i) == null ? 0 : list.get(i).getListGoodsData().size();
    }

    @Override
    public SaleOrderByPhOrderBean getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public SaleOrderByPhOrderBean getChild(int i, int i1) {
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
                    inflate(R.layout.item_rollout_operation_records_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (b) {
            holder.mIvArrow.setImageResource(R.mipmap.top_arrow);
        } else {
            holder.mIvArrow.setImageResource(R.mipmap.bottom_arrow);
        }
        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvOrderCode.setText(list.get(i).getOrderCode());
        holder.mTvTotalAmount.setText(list.get(i).getTotalAmount());
        holder.mTvSalesmain.setText("业务员:" + list.get(i).getSalesName());
        int num = list.get(i).getListGoodsData() == null ? 0 : list.get(i).getListGoodsData().size();
        holder.mTvTotalNum.setText("共" + num + "件商品");
        if ("2".equals(type)) {//销售
            holder.mTvTotalAmountTitle.setText("销售金额:");
            holder.mTvOrderTime.setText("销售时间:" + list.get(i).getAddTime());
        } else {
            holder.mTvTotalAmountTitle.setText("撤货金额:");
            holder.mTvOrderTime.setText("撤货时间:" + list.get(i).getAddTime());
        }
        return view;
    }


    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_order_time)
        TextView mTvOrderTime;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.iv_arrow)
        ImageView mIvArrow;
        @BindView(R.id.tv_total_num)
        TextView mTvTotalNum;
        @BindView(R.id.tv_total_amount_title)
        TextView mTvTotalAmountTitle;
        @BindView(R.id.tv_salesmain)
        TextView mTvSalesmain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_rollout_operation_records_child, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }
        SaleOrderByPhOrderBean bean = list.get(i).getListGoodsData().get(i1);

        holder.mTvGoodName.setText(bean.getGoodsName());
        String priceDesc;
        if ("1".equals(bean.getUnitNum()) || "".equals(bean.getUnitNum())) {
            priceDesc = bean.getMinPrice() + "/" + bean.getMinName();
        } else {
            priceDesc = bean.getMaxPrice() + "/" + bean.getMaxName() + " " +
                    bean.getMinPrice() + "/" + bean.getMinName();
        }
        holder.mTvPriceDesc.setText(priceDesc);
        if ("2".equals(type)) {//销售
            holder.mTvTotalnumDesc.setText("销售:" + bean.getPhSaleNum_desc());
        } else {
            holder.mTvTotalnumDesc.setText("撤货:" + bean.getRefundNum_desc());
        }
        return view;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ViewHolderChild {
        @BindView(R.id.tv_good_name)
        TextView mTvGoodName;
        @BindView(R.id.tv_price_desc)
        TextView mTvPriceDesc;
        @BindView(R.id.tv_totalnum_desc)
        TextView mTvTotalnumDesc;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
