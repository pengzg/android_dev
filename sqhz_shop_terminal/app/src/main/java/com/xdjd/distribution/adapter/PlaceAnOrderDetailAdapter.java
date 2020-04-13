package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.OrderDetailBean;
import com.xdjd.distribution.bean.PlaceAnOrderDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PlaceAnOrderDetailAdapter extends BaseAdapter {

    private List<PlaceAnOrderDetailBean> list;

    public void setData(List<PlaceAnOrderDetailBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
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
                    inflate(R.layout.item_place_an_order_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(list.get(i).getOad_goods_name());
        if ("1".equals(list.get(i).getOad_unit_num())) {//小单位
            if (0 == list.get(i).getOrder_surplus_num()) {
                holder.mTvNoDeliveryNumNameref.setText("发货已完成");
            } else {
                holder.mTvNoDeliveryNumNameref.setText("未发货:" + list.get(i).getOrder_surplus_num() + list.get(i).getOad_goods_unitname_min());
            }

            holder.mTvGoodsNumNameref.setText("总数量:" + list.get(i).getOad_goods_num() + list.get(i).getOad_goods_unitname_min());
        } else {
            if (0 == list.get(i).getOrder_surplus_num()) {
                holder.mTvNoDeliveryNumNameref.setText("发货已完成");
            } else {
                holder.mTvNoDeliveryNumNameref.setText("未发货:" + UnitCalculateUtils.unitStr(list.get(i).getOad_unit_num(),
                        String.valueOf(list.get(i).getOrder_surplus_num()),
                        list.get(i).getOad_goods_unitname_max(), list.get(i).getOad_goods_unitname_min()));
            }
            holder.mTvGoodsNumNameref.setText("总数量:" + UnitCalculateUtils.unitStr(list.get(i).getOad_unit_num(), list.get(i).getOad_goods_num(),
                    list.get(i).getOad_goods_unitname_max(), list.get(i).getOad_goods_unitname_min()));
        }

        holder.mTvTotalPrice.setText("¥:" + list.get(i).getOad_total_amount());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_goods_num_nameref)
        TextView mTvGoodsNumNameref;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;
        @BindView(R.id.tv_no_delivery_num_nameref)
        TextView mTvNoDeliveryNumNameref;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
