package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ApplyOrderBean;
import com.xdjd.view.AnimatedExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ApplyOrderAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    List<ApplyOrderBean> list;
    public int index = 0;
    public int index2 = 0;

    private OnApplyOrderListener listener;

    public interface OnApplyOrderListener {
        void onArrowItem(int i, ImageView iv);
    }

    public ApplyOrderAdapter(OnApplyOrderListener listener) {
        this.listener = listener;
    }

    public void setData(List<ApplyOrderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i).getListData() == null ? 0 : list.get(i).getListData().size();
    }


    @Override
    public ApplyOrderBean getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public Object getChild(int i, int i1) {
        return list.get(i).getListData().get(i1);
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
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_order_goods_one, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvCode.setText(list.get(i).getOa_applycode());
        holder.mTvDate.setText(list.get(i).getOa_applydate());
        holder.mTvRemark.setText("备注:" + list.get(i).getOa_remarks());

        final ViewHolder holder1 = holder;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onArrowItem(i, holder1.mIvArrow);
            }
        });

        return view;
    }

    class ViewHolder {
        @BindView(R.id.iv_arrow)
        ImageView mIvArrow;
        @BindView(R.id.ll_arrow)
        LinearLayout mLlArrow;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_remark)
        TextView mTvRemark;
        @BindView(R.id.tv_date)
        TextView mTvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_order_goods_two, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }

        ApplyOrderBean bean = list.get(i).getListData().get(i1);

        holder.mTvIndex.setText((i1 + 1) + "");
        holder.mTvGoodsName.setText(bean.getOad_goods_name());

        holder.mTvNum.setText(bean.getGoods_num_desc());
        holder.mTvTotalAmount.setText("¥" + bean.getOrder_surplus_total());

        return view;
    }


    //获得子列表项，比较重要、难理解的 一个方法
    //    @Override
    //    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
    //
    //    }

    class ViewHolderChild {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_num)
        TextView mTvNum;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.ll_child)
        LinearLayout mLlChild;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
