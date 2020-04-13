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

public class OrderSettlementAdapter extends BaseAdapter {


    public List<OrderSettlementBean> list;

    public void setData(List<OrderSettlementBean> list) {
        this.list = list;
        notifyDataSetChanged();
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

        String goodsName;
        if ("1".equals(list.get(i).getOd_goods_state()) || list.get(i).getOd_goods_state() == null
                || list.get(i).getOd_goods_state().length() == 0){
            goodsName = list.get(i).getOd_goods_name();
        }else{
            goodsName = "["+list.get(i).getOd_goods_state_nameref()+"]"+ list.get(i).getOd_goods_name();
        }

        holder.mTvName.setText(goodsName);

        BigDecimal maxPrice;
        BigDecimal minPrice;

        if ("1".equals(list.get(i).getOd_unit_num())){

            if (TextUtils.isEmpty(list.get(i).getOd_price_min())){
                minPrice = BigDecimal.ZERO;
            }else{
                minPrice = new BigDecimal(list.get(i).getOd_price_min());
            }

            holder.mTvPriceUnitNameref.setText(minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getOd_goods_unitname_min());
            holder.mTvGoodsNumNameref.setText(list.get(i).getOd_goods_num_min()+list.get(i).getOd_goods_unitname_min());
        }else{

            if (TextUtils.isEmpty(list.get(i).getOd_price_max())){
                maxPrice = BigDecimal.ZERO;
            }else{
                maxPrice = new BigDecimal(list.get(i).getOd_price_max());
            }

            if (TextUtils.isEmpty(list.get(i).getOd_price_min())){
                minPrice = BigDecimal.ZERO;
            }else{
                minPrice = new BigDecimal(list.get(i).getOd_price_min());
            }

            holder.mTvPriceUnitNameref.setText(maxPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getOd_goods_unitname_max()
                    + minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getOd_goods_unitname_min());
            holder.mTvGoodsNumNameref.setText(list.get(i).getOd_goods_num_max()+list.get(i).getOd_goods_unitname_max()+
                    list.get(i).getOd_goods_num_min()+list.get(i).getOd_goods_unitname_min());
        }

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
