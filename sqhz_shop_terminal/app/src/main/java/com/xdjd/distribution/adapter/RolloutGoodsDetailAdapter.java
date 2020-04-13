package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 *     desc   : 铺货销售和撤货详情
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsDetailAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    List<PHOrderDetailBean> list;
    private int type;// 1:铺货单详情 2：铺货销售单详情 3：撤货单详情

    public RolloutGoodsDetailAdapter(int type) {
        this.type = type;
    }

    public void setData(List<PHOrderDetailBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i) == null ? 0 : list.get(i).getListData().size();
    }


    @Override
    public PHOrderDetailBean getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public PHOrderDetailBean getChild(int i, int i1) {
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_rollout_detail_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PHOrderDetailBean bean = list.get(i);
        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(bean.getApply_order_code());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_rollout_detail_child, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }

        PHOrderDetailBean bean = list.get(i).getListData().get(i1);

        holder.mTvName.setText(bean.getGoodsName());
        if ("1".equals(bean.getUnitNum())) {
            holder.mTvMaxpriceDesc.setText("");
            holder.mTvMinpriceDesc.setText(bean.getMinPrice() + "元/" + bean.getMinName());//小单位价格描述
        } else {
            holder.mTvMaxpriceDesc.setText(bean.getMaxPrice() + "元/" + bean.getMaxName());//大单位价格描述
            holder.mTvMinpriceDesc.setText(bean.getMinPrice() + "元/" + bean.getMinName());//小单位价格描述
        }
        switch (type) {
            case 2://销售
                holder.mTvTotalnumDesc.setText(bean.getPhSaleNum_desc());
                break;
            case 3://撤货
                holder.mTvTotalnumDesc.setText(bean.getRefundNum_desc());
                break;
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }



    class ViewHolderChild {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_maxprice_desc)
        TextView mTvMaxpriceDesc;
        @BindView(R.id.tv_minprice_desc)
        TextView mTvMinpriceDesc;
        @BindView(R.id.tv_totalnum_desc)
        TextView mTvTotalnumDesc;

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
