package com.xdjd.distribution.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.BrandAmountBean;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/6/4.
 */

public class BrandPriceAdapter extends BaseAdapter {

    List<BrandAmountBean> list;

    BrandPriceListener listener;

    private EditText mEtBalancePrice;
    private EditText mEtReceivablePrice;

    private int index;

    public BrandPriceAdapter(BrandPriceListener listener) {
        this.listener = listener;
    }

    public void setData(List<BrandAmountBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_price, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvBrandName.setText(list.get(position).getBrandname());
        holder.mTvBrandBalance.setText(list.get(position).getYeamount());

        holder.mTvDeliveryAmount.setText(list.get(position).getTotalamount());

        if ("".equals(list.get(position).getYEAmount()) || list.get(position).getYEAmount() == null){
            list.get(position).setYEAmount("0.00");
        }
        holder.mEtBalancePrice.setText(list.get(position).getYEAmount());

        if ("".equals(list.get(position).getYSAmount()) || list.get(position).getYSAmount() == null){
            list.get(position).setYSAmount("0.00");
        }
        holder.mEtReceivablePrice.setText(list.get(position).getYSAmount());

        BigDecimal bigYeamount = new BigDecimal(list.get(position).getYeamount());
//        if (bigYeamount.doubleValue() > 0){//余额大于0才可编辑
//            holder.mEtBalancePrice.setEnabled(true);//可编辑
//        }else{
//            holder.mEtBalancePrice.setEnabled(false);//不可编辑
//        }
//
//        if (list.get(position).getBd_is_main().equals("Y")){
//            holder.mEtReceivablePrice.setEnabled(false);
//        }else{
//            holder.mEtReceivablePrice.setEnabled(true);
//        }

        final ViewHolder holder2 = holder;
        holder.mEtBalancePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    index = position;
                    mEtBalancePrice = holder2.mEtBalancePrice;
                    mEtReceivablePrice = holder2.mEtReceivablePrice;
                    holder2.mEtBalancePrice.addTextChangedListener(textBalancePrice);
                    holder2.mEtBalancePrice.setSelection(holder2.mEtBalancePrice.getText().length());
                } else {
                    holder2.mEtBalancePrice.removeTextChangedListener(textBalancePrice);
                }
            }
        });

        holder.mEtReceivablePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    index = position;
                    mEtBalancePrice = holder2.mEtBalancePrice;
                    mEtReceivablePrice = holder2.mEtReceivablePrice;
                    holder2.mEtReceivablePrice.addTextChangedListener(textReceivablePrice);
                    holder2.mEtReceivablePrice.setSelection(holder2.mEtReceivablePrice.getText().length());
                } else {
                    holder2.mEtReceivablePrice.removeTextChangedListener(textReceivablePrice);
                }
            }
        });

        return view;
    }

    private TextWatcher textBalancePrice = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onBalancePrice(index,editable.toString(),mEtBalancePrice,mEtReceivablePrice);
        }
    };

    private TextWatcher textReceivablePrice = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onReceivablePrice(index,editable.toString(),mEtBalancePrice,mEtReceivablePrice);
        }
    };

    public interface BrandPriceListener{
        /** 余额 */
        void onBalancePrice(int i,String balancePrice,EditText mEtBalancePrice ,EditText mEtReceivablePrice);
        /** 应收款 */
        void onReceivablePrice(int i,String receivablePrice,EditText mEtBalancePrice ,EditText mEtReceivablePrice);
    }

    class ViewHolder {
        @BindView(R.id.tv_brand_name)
        TextView mTvBrandName;
        @BindView(R.id.tv_brand_balance)
        TextView mTvBrandBalance;
        @BindView(R.id.tv_balance_name)
        TextView mTvBalanceName;
        @BindView(R.id.et_balance_price)
        EditText mEtBalancePrice;
        @BindView(R.id.tv_receivable_name)
        TextView mTvReceivableName;
        @BindView(R.id.et_receivable_price)
        EditText mEtReceivablePrice;
        @BindView(R.id.tv_delivery_amount)
        TextView mTvDeliveryAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
