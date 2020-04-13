package com.xdjd.distribution.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.GoodsBean;

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

public class NewRolloutMakesureOrderAdapter extends BaseAdapter {


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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_new_rollout_makesure_order, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvName.setText(list.get(i).getGg_title());

        BigDecimal maxPrice;
        BigDecimal minPrice;

        if ("1".equals(list.get(i).getGgp_unit_num())) {

            if (TextUtils.isEmpty(list.get(i).getMinPrice())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(list.get(i).getMinPrice());
            }

            holder.mTvPriceUnitNameref.setText(minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + list.get(i).getGg_unit_min_nameref());
            holder.mTvGoodsNumNameref.setText(list.get(i).getMinNum() + list.get(i).getGg_unit_min_nameref());
        } else {

            if (TextUtils.isEmpty(list.get(i).getMaxPrice())) {
                maxPrice = BigDecimal.ZERO;
            } else {
                maxPrice = new BigDecimal(list.get(i).getMaxPrice());
            }

            if (TextUtils.isEmpty(list.get(i).getMinPrice())) {
                minPrice = BigDecimal.ZERO;
            } else {
                minPrice = new BigDecimal(list.get(i).getMinPrice());
            }

            holder.mTvPriceUnitNameref.setText(maxPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + list.get(i).getGg_unit_max_nameref()
                    + minPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + "/" + list.get(i).getGg_unit_min_nameref());

            holder.mTvGoodsNumNameref.setText(list.get(i).getMaxNum() + list.get(i).getGg_unit_max_nameref() +
                    list.get(i).getMinNum() + list.get(i).getGg_unit_min_nameref());
        }

        holder.mTvTotalPrice.setText("¥:" + list.get(i).getTotalPrice());
       /* String goodsName;
        if ("1".equals(list.get(i).getOd_goods_state()) || list.get(i).getOd_goods_state() == null
                || list.get(i).getOd_goods_state().length() == 0){
            goodsName = list.get(i).getOd_goods_name();
        }else{
            goodsName = "["+list.get(i).getOd_goods_state_nameref()+"]"+ list.get(i).getOd_goods_name();
        }*/

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
