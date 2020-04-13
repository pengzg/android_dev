package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.StockOutBean;
import com.xdjd.distribution.callback.ItemOnListener;

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

public class OutboundQueryAdapter extends BaseAdapter {

    List<StockOutBean> list;
    private ItemOnListener listener;
    private String indexType="";//303退货申请时不显示订单

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public OutboundQueryAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<StockOutBean> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_outbound_query, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvEimCode.setText(list.get(i).getEim_code());
        holder.mTvOrderState.setText(list.get(i).getEim_stats_nameref());

        holder.mTvOperation.setText(/*"业务员:" + */list.get(i).getEim_salesid_nameref() == null ? "" : list.get(i).getEim_salesid_nameref());

        holder.mTvCustomerName.setText(list.get(i).getEim_customerid_nameref());
        if ("303".equals(indexType) || "302".equals(indexType)){//退货申请和要货申请
            holder.mTvOrderCode.setVisibility(View.GONE);
        }else{
            holder.mTvOrderCode.setVisibility(View.VISIBLE);
            holder.mTvOrderCode.setText("订单编号:"+list.get(i).getEim_source_code());
        }
        holder.mTvDate.setText("订单日期: " + list.get(i).getEim_billdate());
        holder.mTvAmount.setText("订单金额: ¥" + list.get(i).getEim_totalamount());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_order_state)
        TextView mTvOrderState;
        @BindView(R.id.tv_operation)
        TextView mTvOperation;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_amount)
        TextView mTvAmount;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_eim_code)
        TextView mTvEimCode;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
