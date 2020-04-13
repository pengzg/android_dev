package com.xdjd.distribution.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.DisplayInDetailBean;
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

public class DisplayFeeSettlementAdapter extends BaseAdapter {


    public List<GoodsBean> list;//陈列出库结算时的参数
    public List<DisplayInDetailBean> listDetail;
    public int type;//1:陈列出库商品列表;2:陈列详情商品列表

    public DisplayFeeSettlementAdapter(int type) {
        this.type = type;
    }

    public void setListDetail(List<DisplayInDetailBean> listDetail) {
        this.listDetail = listDetail;
        notifyDataSetChanged();
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return type==1?(list == null ? 0 : list.size()):(listDetail == null ? 0 : listDetail.size());
    }

    @Override
    public Object getItem(int i) {
        return type==1?list.get(i):listDetail.get(i);
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
                    inflate(R.layout.item_display_fee_settlement, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (type==1){
            holder.mTvName.setText(list.get(i).getGg_title());
            BigDecimal maxPrice;
            BigDecimal minPrice;
            if ("1".equals(list.get(i).getGgp_unit_num())){
                if (TextUtils.isEmpty(list.get(i).getMinPrice())){
                    minPrice = BigDecimal.ZERO;
                }else{
                    minPrice = new BigDecimal(list.get(i).getMinPrice());
                }
                holder.mTvPriceUnitNameref.setText(minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getGg_unit_min_nameref());
                holder.mTvGoodsNumNameref.setText(list.get(i).getMinNum()+list.get(i).getGg_unit_min_nameref());
            }else{
                if (TextUtils.isEmpty(list.get(i).getMaxPrice())){
                    maxPrice = BigDecimal.ZERO;
                }else{
                    maxPrice = new BigDecimal(list.get(i).getMaxPrice());
                }
                if (TextUtils.isEmpty(list.get(i).getMinPrice())){
                    minPrice = BigDecimal.ZERO;
                }else{
                    minPrice = new BigDecimal(list.get(i).getMinPrice());
                }
                holder.mTvPriceUnitNameref.setText(maxPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getGg_unit_max_nameref()
                        + minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+list.get(i).getGg_unit_min_nameref());
                holder.mTvGoodsNumNameref.setText(list.get(i).getMaxNum()+list.get(i).getGg_unit_max_nameref()+
                        list.get(i).getMinNum()+list.get(i).getGg_unit_min_nameref());
            }
            holder.mTvTotalPrice.setText("¥:"+list.get(i).getTotalPrice());
        }else{
            DisplayInDetailBean bean = listDetail.get(i);
            holder.mTvName.setText(bean.getEii_goods_name());
            BigDecimal maxPrice;
            BigDecimal minPrice;
            if ("1".equals(bean.getEii_unit_num())){
                if (TextUtils.isEmpty(bean.getEii_goods_price_min())){
                    minPrice = BigDecimal.ZERO;
                }else{
                    minPrice = new BigDecimal(bean.getEii_goods_price_min());
                }
                holder.mTvPriceUnitNameref.setText(minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+
                        "/"+bean.getEii_unit_min());
                holder.mTvGoodsNumNameref.setText(bean.getEii_goods_quantity_min() + bean.getEii_unit_min());
            }else{
                if (TextUtils.isEmpty(bean.getEii_goods_price_max())){
                    maxPrice = BigDecimal.ZERO;
                }else{
                    maxPrice = new BigDecimal(bean.getEii_goods_price_max());
                }
                if (TextUtils.isEmpty(bean.getEii_goods_price_min())){
                    minPrice = BigDecimal.ZERO;
                }else{
                    minPrice = new BigDecimal(bean.getEii_goods_price_min());
                }
                String unitStr = maxPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+bean.getEii_unit_max()
                        + minPrice.setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/"+bean.getEii_unit_min();
                String numStr = bean.getEii_goods_quantity_max() + bean.getEii_unit_max()+
                        bean.getEii_goods_quantity_min() + bean.getEii_unit_min();
                holder.mTvPriceUnitNameref.setText(unitStr);
                holder.mTvGoodsNumNameref.setText(numStr);
            }
            holder.mTvTotalPrice.setText("¥:"+bean.getEii_goods_amount());
        }

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
