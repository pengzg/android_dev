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
import com.xdjd.distribution.bean.GoodsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/10
 *     desc   : 配送商品列表adapter
 *     version: 1.0
 * </pre>
 */

public class DistributionGoodsAdapter extends BaseAdapter {

    public List<GoodsBean> list;

    private DistributionGoodsListener listener;

    private int index;

    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private TextView tvSumPrice;

    public DistributionGoodsAdapter(DistributionGoodsListener listener) {
        this.listener = listener;
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public GoodsBean getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_distribution_goods_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText(String.valueOf(i + 1));
        holder.mTvName.setText(list.get(i).getGg_title());
        holder.mTvModel.setText(list.get(i).getGg_model());
        holder.mTvStock.setText(list.get(i).getStock_nameref());
        holder.mTvMaxUnit.setText(list.get(i).getMax_price() + "元/" + list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnit.setText(list.get(i).getMin_price() + "元/" + list.get(i).getGg_unit_min_nameref());

        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());

        holder.mTvTotalPrice.setText("¥:" + list.get(i).getTotalPrice());

        final ViewHolder holder1 = holder;

        holder.mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClearNum(i,holder1.mEtMaxNum,holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mTvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxMinus(i,holder1.mEtMaxNum,holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mTvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxPlus(i,holder1.mEtMaxNum,holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mTvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinMinus(i,holder1.mEtMaxNum,holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mTvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinPlus(i,holder1.mEtMaxNum,holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        final ViewHolder holder2 = holder;
        holder.mEtMaxNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    mEtMaxNum = holder2.mEtMaxNum;
                    mEtMinNum = holder2.mEtMinNum;
                    tvSumPrice = holder2.mTvTotalPrice;
                    holder2.mEtMaxNum.addTextChangedListener(textMaxWatcher);
                } else {
                    holder2.mEtMaxNum.removeTextChangedListener(textMaxWatcher);
                }
            }
        });

        holder.mEtMinNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    mEtMaxNum = holder2.mEtMaxNum;
                    mEtMinNum = holder2.mEtMinNum;
                    tvSumPrice = holder2.mTvTotalPrice;
                    holder2.mEtMinNum.addTextChangedListener(textMinWatcher);
                } else {
                    holder2.mEtMinNum.removeTextChangedListener(textMinWatcher);
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
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onMaxEtNum(index,editable.toString(),mEtMaxNum,mEtMinNum,tvSumPrice);
        }
    };

    private TextWatcher textMinWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onMinEtNum(index,editable.toString(),mEtMaxNum,mEtMinNum,tvSumPrice);
        }
    };

    public interface DistributionGoodsListener {
        void onMaxPlus(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_model)
        TextView mTvModel;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;
        @BindView(R.id.et_max_num)
        EditText mEtMaxNum;
        @BindView(R.id.et_min_num)
        EditText mEtMinNum;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;
        @BindView(R.id.tv_clear)
        TextView mTvClear;
        @BindView(R.id.tv_max_minus)
        TextView mTvMaxMinus;
        @BindView(R.id.tv_max_plus)
        TextView mTvMaxPlus;
        @BindView(R.id.tv_max_unit_name)
        TextView mTvMaxUnitName;
        @BindView(R.id.tv_min_minus)
        TextView mTvMinMinus;
        @BindView(R.id.tv_min_plus)
        TextView mTvMinPlus;
        @BindView(R.id.tv_min_unit_name)
        TextView mTvMinUnitName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
