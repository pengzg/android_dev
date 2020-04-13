package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.CarStorageBean;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CarStoreAdapter extends BaseAdapter {

    public List<CarStorageBean> list;

    public void setData(List<CarStorageBean> list) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_store, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText(i + 1 + "");
        holder.mTvName.setText(list.get(i).getGg_title());
        //        holder.mTvTotalPrice.setText("¥"+list.get(i).getGgp_price());
        holder.mTvMaxPrice.setText(list.get(i).getMax_price_desc());
        holder.mTvMinPrice.setText(list.get(i).getMin_price_desc());

        holder.mTvUsableMaxStore.setVisibility(View.VISIBLE);
        holder.mTvMaxPrice.setVisibility(View.VISIBLE);

        if ("".equals(list.get(i).getGgp_unit_num()) || list.get(i).getGgp_unit_num() == null) {
            holder.mTvUsableMaxStore.setText("");
            holder.mTvUsableMinStore.setText(list.get(i).getGgs_stock() + list.get(i).getGg_unit_min_nameref());

            holder.mTvYesterdayOutboundMaxNum.setText("");
            holder.mTvYesterdayOutboundMinNum.setText(list.get(i).getOutstock_num_now() + "/" + list.get(i).getGg_unit_min_nameref());

            holder.mTvTodayOutboundMaxNum.setText("");
            holder.mTvTodayOutboundMinNum.setText(list.get(i).getOutstock_num_yes() + "/" + list.get(i).getGg_unit_min_nameref());
        } else {
            BigDecimal unitNum = new BigDecimal(list.get(i).getGgp_unit_num());

            if (unitNum.intValue() == 1){
                holder.mTvUsableMinStore.setText(list.get(i).getGgs_stock() + list.get(i).getGg_unit_min_nameref());
                holder.mTvUsableMaxStore.setVisibility(View.GONE);
                holder.mTvMaxPrice.setVisibility(View.GONE);
            }else{
                if ("".equals(list.get(i).getGgs_stock()) || list.get(i).getGgs_stock() == null) {
                    holder.mTvUsableMaxStore.setText("");
                    holder.mTvUsableMinStore.setText("");
                } else {
                    BigDecimal bigStock = new BigDecimal(list.get(i).getGgs_stock());
                    //BigDecimal bdInteger = bdStock.divideToIntegralValue(bdUnitNum); //取整
                    //该方法接收另一个BigDecimal 对象作为参数，该参数即为除数，
                    // 返回一个BigDecimal数组，返回数组中包含两个元素，第一个元素为两数相除的商，第二个元素为余数。
                    BigDecimal[] results = bigStock.divideAndRemainder(unitNum);
                    holder.mTvUsableMaxStore.setText(results[0] + list.get(i).getGg_unit_max_nameref());
                    holder.mTvUsableMinStore.setText(results[1] + list.get(i).getGg_unit_min_nameref());
                }
            }

            //            if ("".equals(list.get(i).getOutstock_num_now()) || list.get(i).getOutstock_num_now() == null){
            //                holder.mTvYesterdayOutboundMaxNum.setText("");
            //                holder.mTvYesterdayOutboundMinNum.setText("");
            //            }else{
            //                BigDecimal bigNowOutstock = new BigDecimal(list.get(i).getOutstock_num_now());
            //
            //                BigDecimal[] results = bigNowOutstock.divideAndRemainder(unitNum);
            //                holder.mTvYesterdayOutboundMaxNum.setText(results[0]+"/"+list.get(i).getGg_unit_max_nameref());
            //                holder.mTvYesterdayOutboundMinNum.setText(results[1]+"/"+list.get(i).getGg_unit_min_nameref());
            //            }
            //
            //            if ("".equals(list.get(i).getOutstock_num_yes()) || list.get(i).getOutstock_num_yes() == null){
            //                holder.mTvTodayOutboundMaxNum.setText("");
            //                holder.mTvTodayOutboundMinNum.setText("");
            //            }else{
            //                BigDecimal bigYesOutstock = new BigDecimal(list.get(i).getOutstock_num_yes());
            //
            //                BigDecimal[] results = bigYesOutstock.divideAndRemainder(unitNum);
            //                holder.mTvTodayOutboundMaxNum.setText(results[0]+"/"+list.get(i).getGg_unit_max_nameref());
            //                holder.mTvTodayOutboundMinNum.setText(results[1]+"/"+list.get(i).getGg_unit_min_nameref());
            //            }
        }

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;
        @BindView(R.id.tv_usable_max_store)
        TextView mTvUsableMaxStore;
        @BindView(R.id.tv_usable_min_store)
        TextView mTvUsableMinStore;
        @BindView(R.id.tv_yesterday_outbound_max_num)
        TextView mTvYesterdayOutboundMaxNum;
        @BindView(R.id.tv_yesterday_outbound_min_num)
        TextView mTvYesterdayOutboundMinNum;
        @BindView(R.id.tv_today_outbound_max_num)
        TextView mTvTodayOutboundMaxNum;
        @BindView(R.id.tv_today_outbound_min_num)
        TextView mTvTodayOutboundMinNum;
        @BindView(R.id.tv_max_price)
        TextView mTvMaxPrice;
        @BindView(R.id.tv_min_price)
        TextView mTvMinPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
