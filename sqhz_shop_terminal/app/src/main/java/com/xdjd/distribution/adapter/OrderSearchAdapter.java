package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class OrderSearchAdapter extends BaseAdapter {

    public List<OrderBean> list;

    private ItemOnListener listener;

    public OrderSearchAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<OrderBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public OrderBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_search, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvOrderCode.setText(list.get(i).getOm_ordercode());
        holder.mTvOrderState.setText(list.get(i).getOm_stats_nameref());

        holder.mTvCustomerName.setText(list.get(i).getOm_customerid_nameref());
        holder.mTvOrderType.setText(list.get(i).getOm_ordertype_nameref());
        holder.mTvOrderAmount.setText("订单金额: ¥" + list.get(i).getOm_order_amount());
        holder.mTvOrderDate.setText("下单日期: " + list.get(i).getOm_orderdate());
        holder.mTvStaffName.setText("业务员:" + list.get(i).getOm_salesid_nameref());
        holder.mTvStock.setText(list.get(i).getOm_storeid_nameref());
        if (list.get(i).getOm_clerkid_nameref() == null || "".equals(list.get(i).getOm_clerkid_nameref())){
            holder.mTvDistributionName.setVisibility(View.GONE);
        }else{
            holder.mTvDistributionName.setVisibility(View.VISIBLE);
            holder.mTvDistributionName.setText("配送员:" + list.get(i).getOm_clerkid_nameref());
        }

        BigDecimal deliveryAmount = BigDecimal.ZERO;
        BigDecimal settAmount = BigDecimal.ZERO;
        if (list.get(i).getOm_delivery_amount() != null && list.get(i).getOm_delivery_amount().length() > 0){
            deliveryAmount = new BigDecimal(list.get(i).getOm_delivery_amount());
        }
        if (list.get(i).getOm_sett_amount() != null && list.get(i).getOm_sett_amount().length() > 0){
            settAmount = new BigDecimal(list.get(i).getOm_sett_amount());
        }

        if (deliveryAmount.compareTo(settAmount) == 1){
            holder.mTvSettAmount.setTextColor(UIUtils.getColor(R.color.text_dark_red));
        }else{
            holder.mTvSettAmount.setTextColor(UIUtils.getColor(R.color.text_black_212121));
        }

        holder.mTvDeliveryAmount.setText("发货金额: ¥" + list.get(i).getOm_delivery_amount());
        holder.mTvSettAmount.setText("收款金额: ¥" + list.get(i).getOm_sett_amount());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }


    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_order_type)
        TextView mTvOrderType;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.tv_order_date)
        TextView mTvOrderDate;
        @BindView(R.id.tv_staff_name)
        TextView mTvStaffName;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.tv_distribution_name)
        TextView mTvDistributionName;
        @BindView(R.id.tv_delivery_amount)
        TextView mTvDeliveryAmount;
        @BindView(R.id.tv_sett_amount)
        TextView mTvSettAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
