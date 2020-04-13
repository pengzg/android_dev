package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.CashReportBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/5/29.
 */

public class CashReportAdapter extends BaseAdapter {

    public List<CashReportBean> list;

    public void setData(List<CashReportBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cash_report, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        CashReportBean bean = list.get(i);

        holder.mCustmerName.setText(bean.getCustmerName());
        holder.mTotalAmount.setText("¥"+bean.getTotalAmount());
//        holder.mCustomerCode.setText(bean.getCustomerCode());
//        holder.mCustomerTypeName.setText(bean.getCustomerTypeName());
        holder.mPaidAmount.setText("¥"+bean.getPaidAmount());
        holder.mRespondReceivableAmount.setText("¥"+bean.getRespondReceivableAmount());
        holder.mUseAmount.setText("¥"+bean.getUseAmount());
        holder.mDiscountAmount.setText("¥"+bean.getDiscountAmount());
        holder.mCardFee.setText("¥"+bean.getCardFee());

        holder.mCardAmount.setText("¥"+bean.getCardAmount());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.custmerName)
        TextView mCustmerName;
        @BindView(R.id.totalAmount)
        TextView mTotalAmount;
//        @BindView(R.id.customerCode)
//        TextView mCustomerCode;
//        @BindView(R.id.customerTypeName)
//        TextView mCustomerTypeName;
        @BindView(R.id.paidAmount)
        TextView mPaidAmount;
        @BindView(R.id.cardAmount)
        TextView mCardAmount;
        @BindView(R.id.respondReceivableAmount)
        TextView mRespondReceivableAmount;
        @BindView(R.id.useAmount)
        TextView mUseAmount;
        @BindView(R.id.discountAmount)
        TextView mDiscountAmount;
        @BindView(R.id.cardFee)
        TextView mCardFee;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
