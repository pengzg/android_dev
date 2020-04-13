package com.xdjd.distribution.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.distribution.bean.OrderSettlementBean;

import java.math.BigDecimal;
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

public class InventoryGoodsAdapter extends BaseAdapter {


    public List<GoodsBean> list;

    public void setData(List<GoodsBean> list) {
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
                    inflate(R.layout.item_inventory_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvName.setText(list.get(i).getGg_title() + list.get(i).getGg_model());

        if ("1".equals(list.get(i).getGgp_unit_num())){
            holder.mTvPriceUnitNameref.setText(list.get(i).getMinPrice()+"/"+list.get(i).getGg_unit_min_nameref());
            holder.mTvGoodsNumNameref.setText(list.get(i).getMinNum()+list.get(i).getGg_unit_min_nameref());
        }else{
            holder.mTvPriceUnitNameref.setText(list.get(i).getMaxPrice()+"/"+list.get(i).getGg_unit_max_nameref()
                    + list.get(i).getMinPrice()+"/"+list.get(i).getGg_unit_min_nameref());
            holder.mTvGoodsNumNameref.setText(list.get(i).getMaxNum()+list.get(i).getGg_unit_max_nameref()+
                    list.get(i).getMinNum()+list.get(i).getGg_unit_min_nameref());
        }

        holder.mTvTotalPrice.setText("¥:"+list.get(i).getTotalPrice());

        return view;
    }

    class ViewHolder {
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
