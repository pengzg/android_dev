package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ReceiptPaymentDetailBean;

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

public class ReceiptPaymentDetailAdapter extends BaseAdapter {

    private List<ReceiptPaymentDetailBean> list;
    public ReceiptPaymentDetailListener listener;

    public ReceiptPaymentDetailAdapter(ReceiptPaymentDetailListener listener) {
        this.listener = listener;
    }

    public void setData(List<ReceiptPaymentDetailBean> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_receipt_payment_detail, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText((i + 1) + "");

        holder.mTvCode.setText(list.get(i).getBillCode());
        holder.mTvOrderCode.setText(list.get(i).getBillDate());

        if (list.get(i).getGc_stats_nameref() == null || list.get(i).getGc_stats_nameref().length() == 0) {
            holder.mLlStatus.setVisibility(View.GONE);
        } else {
            holder.mLlStatus.setVisibility(View.VISIBLE);
            holder.mTvStatus.setText(list.get(i).getGc_stats_nameref());
        }

        holder.mTvCounterName.setText(list.get(i).getCounterName());
        holder.mTvType.setText(list.get(i).getTypeName());
        holder.mTvSkAmount.setText("刷卡:¥" + (list.get(i).getCardAmount() == null ? "0.00" : list.get(i).getCardAmount()));
        holder.mTvXjAmount.setText("现金:¥" + (list.get(i).getCashAmount() == null ? "0.00" : list.get(i).getCashAmount()));
        holder.mTvYhAmount.setText("优惠:¥" + (list.get(i).getDiscountAmount() == null ? "0.00" : list.get(i).getDiscountAmount()));

        if (listener != null) {
            if (("2101".equals(list.get(i).getType()) || "2104".equals(list.get(i).getType())) &&
                    "1".equals(list.get(i).getGc_stats())) {// 欠款或收款 && 未审核
                holder.mLlCancelPayment.setVisibility(View.VISIBLE);
                holder.mTvCancelPayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onCancelPayment(i);
                    }
                });
            } else {
                holder.mLlCancelPayment.setVisibility(View.GONE);
            }
        }

        if (list.get(i).getGc_isred()!=null && "Y".equals(list.get(i).getGc_isred())){
            holder.mLlRemarks.setVisibility(View.VISIBLE);
            holder.mTvRemarks.setText(list.get(i).getGc_remarks());
        }else{
            holder.mLlRemarks.setVisibility(View.GONE);
        }

        return view;
    }

    public interface ReceiptPaymentDetailListener {
        void onCancelPayment(int i);
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.tv_sk_amount)
        TextView mTvSkAmount;
        @BindView(R.id.tv_xj_amount)
        TextView mTvXjAmount;
        @BindView(R.id.tv_yh_amount)
        TextView mTvYhAmount;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_counter_name)
        TextView mTvCounterName;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.ll_status)
        LinearLayout mLlStatus;
        @BindView(R.id.tv_cancel_payment)
        TextView mTvCancelPayment;
        @BindView(R.id.ll_cancel_payment)
        LinearLayout mLlCancelPayment;
        @BindView(R.id.tv_remarks)
        TextView mTvRemarks;
        @BindView(R.id.ll_remarks)
        LinearLayout mLlRemarks;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
