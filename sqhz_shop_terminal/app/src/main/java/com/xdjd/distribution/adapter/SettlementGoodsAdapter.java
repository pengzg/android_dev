package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.DistributionGoodsBean;
import com.xdjd.distribution.bean.OrderBean;

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

public class SettlementGoodsAdapter extends BaseAdapter {


    private List<DistributionGoodsBean> list;

    public void setData(List<DistributionGoodsBean> list) {
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
                    inflate(R.layout.item_order_settlement, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvName.setText(list.get(i).getOd_goods_name());
        holder.mTvModel.setText(list.get(i).getGg_model());

        holder.mTvPriceUnitNameref.setText(list.get(i).getOd_price_max()+"/"+list.get(i).getOd_goods_unitname_max());

        holder.mTvGoodsNumNameref.setVisibility(View.INVISIBLE);
        holder.mTvGoodsNumNameref.setText(list.get(i).getOd_goods_num_max()+list.get(i).getOd_goods_unitname_max()+
                list.get(i).getOd_goods_unitname_min()+list.get(i).getOd_goods_unitname_min());

        holder.mTvTotalPrice.setText("¥:"+list.get(i).getOd_real_total());

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
