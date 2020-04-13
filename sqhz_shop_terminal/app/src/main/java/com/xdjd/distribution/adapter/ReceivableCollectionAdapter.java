package com.xdjd.distribution.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ReceivableListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/9/6
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ReceivableCollectionAdapter extends BaseAdapter {

    public List<ReceivableListBean> list;
    OnReceivableListener listener;
    private int index;

    private EditText mEtWsAmount;
    //编辑之前的未收金额
    public String wsBeforeAmount;

    public ReceivableCollectionAdapter(OnReceivableListener listener) {
        this.listener = listener;
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
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_customer_yks_collection, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ReceivableListBean bean = list.get(i);

        if (bean.getIsSelect() == 0) {
            holder.mIvImg.setImageResource(R.drawable.check_true);
        } else {
            holder.mIvImg.setImageResource(R.drawable.check_false);
        }

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvOrderCode.setText(bean.getGr_sourcecode());
        holder.mTvTotalAmount.setText("总金额:¥" + bean.getGr_total_amount());
        holder.mTvTradeAmount.setText("已结算:¥" + bean.getGr_trade_amount());
        holder.mTvWsAmount.setText("未收:¥" + bean.getWs_amount());
        holder.mTvDiscountsAmount.setText("优惠:¥" + bean.getGr_discount_amount());
        holder.mEtReceivablePrice.setText(bean.getEt_ws_amount());

        final ViewHolder holder2;
        holder2 = holder;
        holder.mLlSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onImgSelect(i, holder2.mIvImg);
            }
        });

        holder.mEtReceivablePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    mEtWsAmount = holder2.mEtReceivablePrice;
                    mEtWsAmount.setSelection(holder2.mEtReceivablePrice.length());
                    mEtWsAmount.setSelectAllOnFocus(true);
                    wsBeforeAmount = mEtWsAmount.getText().toString();
                    holder2.mEtReceivablePrice.addTextChangedListener(textMaxWatcher);
                } else {
                    holder2.mEtReceivablePrice.removeTextChangedListener(textMaxWatcher);
                }
            }
        });

        return view;
    }

    private TextWatcher textMaxWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtWsAmount.setText(s);
                    mEtWsAmount.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtWsAmount.setText(s);
                mEtWsAmount.setSelection(2);
            }

            if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    mEtWsAmount.setText(s.subSequence(0, 1));
                    mEtWsAmount.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onWsEtPrice(index, editable.toString(),editable, mEtWsAmount,wsBeforeAmount);
        }
    };

    public void setData(List<ReceivableListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public interface OnReceivableListener {
        void onImgSelect(int i, ImageView ivImg);

        void onWsEtPrice(int index, String s,Editable editable, EditText etWsAmount,String wsBeforeAmount);
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_order_code)
        TextView mTvOrderCode;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_trade_amount)
        TextView mTvTradeAmount;
        @BindView(R.id.tv_ws_amount)
        TextView mTvWsAmount;
        @BindView(R.id.tv_discounts_amount)
        TextView mTvDiscountsAmount;
        @BindView(R.id.iv_img)
        ImageView mIvImg;
        @BindView(R.id.ll_select)
        LinearLayout mLlSelect;
        @BindView(R.id.et_receivable_price)
        EditText mEtReceivablePrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
