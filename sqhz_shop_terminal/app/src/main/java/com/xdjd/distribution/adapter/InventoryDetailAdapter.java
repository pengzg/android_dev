package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.InventoryDetailBean;

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

public class InventoryDetailAdapter extends BaseAdapter {

    public List<InventoryDetailBean> list;

    public void setDate(List<InventoryDetailBean> list) {
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
                    .inflate(R.layout.item_customer_inventory_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        InventoryDetailBean bean = list.get(i);

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(bean.getCii_goods_name());

        if ("1".equals(bean.getCii_unit_num())) {
            holder.mTvNum.setText(bean.getCii_goods_quantity_min() + bean.getCii_unit_min() + "*" + bean.getCii_goods_price_min()
                    + "元" + "= ¥" + bean.getCii_goods_amount());
        } else {
            holder.mTvNum.setText(bean.getCii_goods_quantity_max() + bean.getCii_unit_max() + "*" + bean.getCii_goods_price_max()
                    + "元"
                    + " + " + bean.getCii_goods_quantity_min() + bean.getCii_unit_min()  + "*" + bean.getCii_goods_price_min()
                    + "元" + " = ¥" + bean.getCii_goods_amount());
        }
        holder.mTvDisplayDuitou.setText(bean.getCii_duitou_quantity() + "个堆头," + bean.getCii_display_quantity() + "个陈列面");

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_display_duitou)
        TextView mTvDisplayDuitou;
        @BindView(R.id.tv_num)
        TextView mTvNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
