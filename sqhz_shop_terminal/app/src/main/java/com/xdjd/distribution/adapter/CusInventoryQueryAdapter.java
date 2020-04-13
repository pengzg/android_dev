package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.CusInventoryQueryBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CusInventoryQueryAdapter extends BaseAdapter {

    public List<CusInventoryQueryBean> list;

    public void setDate(List<CusInventoryQueryBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_customer_inventory_query, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CusInventoryQueryBean bean = list.get(i);

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvCode.setText(bean.getCim_code());
        holder.mTvOrderState.setText(bean.getCim_stats_nameref());

        holder.mTvName.setText(bean.getCim_customerid_nameref());
        holder.mTvAddNum.setText("合计:" + bean.getCim_totalnum() + "条");
        holder.mTvAddTime.setText("盘点时间:" + bean.getCim_billdate());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_add_num)
        TextView mTvAddNum;
        @BindView(R.id.tv_add_time)
        TextView mTvAddTime;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
