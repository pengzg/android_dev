package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.OutboundDetailBean;

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

public class OutboundDetailAdapter extends BaseAdapter {

    private List<OutboundDetailBean> list;

    public void setData(List<OutboundDetailBean> list) {
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
                    inflate(R.layout.item_order_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i+1)+"");
        if ("1".equals(list.get(i).getEii_goods_state()) || list.get(i).getEii_goods_state() == null
                || list.get(i).getEii_goods_state().length() == 0){
            holder.mTvName.setText(list.get(i).getEii_goods_name() + list.get(i).getGg_model());
        }else{
            holder.mTvName.setText("["+list.get(i).getEii_goods_state_nameref()+"]"+
                    list.get(i).getEii_goods_name() + list.get(i).getGg_model());
        }

        if ("1".equals(list.get(i).getEii_unit_num())){
            holder.mTvPriceUnitNameref.setText(list.get(i).getEii_goods_price_min() + "元/" + list.get(i).getEii_unit_min());
            holder.mTvGoodsNumNameref.setText(list.get(i).getEii_goods_quantity_min() + list.get(i).getEii_unit_min());
        }else{
            holder.mTvPriceUnitNameref.setText(list.get(i).getEii_goods_price_max() + "元/" + list.get(i).getEii_unit_max());
            holder.mTvGoodsNumNameref.setText(list.get(i).getEii_goods_quantity_max() + list.get(i).getEii_unit_max() +
                    list.get(i).getEii_goods_quantity_min() + list.get(i).getEii_unit_min());
        }

//        if (list.get(i).getEii_goods_price_max() == null || "".equals(list.get(i).getEii_goods_price_max())) {
//            holder.mTvPriceUnitNameref.setText(list.get(i).getEii_goods_price_max() + "元/" + list.get(i).getEii_unitid_max());
//        } else {
//            holder.mTvPriceUnitNameref.setText(list.get(i).getEii_goods_price_min() + "元/" + list.get(i).getEii_unit_min());
//        }

        holder.mTvTotalPrice.setText("¥:" + list.get(i).getEii_goods_amount());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_model)
        TextView mTvModel;
        @BindView(R.id.tv_price_unit_nameref)
        TextView mTvPriceUnitNameref;
        @BindView(R.id.tv_goods_num_nameref)
        TextView mTvGoodsNumNameref;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
