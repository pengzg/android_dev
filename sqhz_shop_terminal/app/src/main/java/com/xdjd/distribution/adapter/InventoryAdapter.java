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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.AnimUtils;

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

public class InventoryAdapter extends BaseAdapter {

    public List<GoodsBean> list;

    private InventoryListener listener;

    private int index;

    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private EditText mEtDisplayNum, mEtDuitouNum;
    private TextView tvSumPrice;

    public InventoryAdapter(InventoryListener listener) {
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
                    inflate(R.layout.item_inventory, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText(String.valueOf(i + 1));
        holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model());
        holder.mTvInventoryDate.setText(list.get(i).getInventory_date());

        //如果大小单位换算比==1隐藏大单位
        if (list.get(i).getGgp_unit_num().equals("1")) {
            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinUnit.setText(list.get(i).getMinPrice() + "元/" + list.get(i).getGg_unit_min_nameref());
        } else {
            holder.mLlMaxMain.setVisibility(View.VISIBLE);

            holder.mTvMaxUnit.setText(list.get(i).getMaxPrice() + "元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText(list.get(i).getMinPrice() + "元/" + list.get(i).getGg_unit_min_nameref());
        }

        holder.mTvMaxUnitName.setText(list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnitName.setText(list.get(i).getGg_unit_min_nameref());

        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());

        holder.mEtDisplayNum.setText(list.get(i).getDisplay_quantity());
        holder.mEtDuitouNum.setText(list.get(i).getDuitou_quantity());

        holder.mTvTotalPrice.setText(list.get(i).getTotalPrice());

        AnimUtils.setTranslateAnimation(holder.mEtMaxNum, holder.mLlMaxLeft, holder.mRlMax, holder.mIvMaxPlus);
        AnimUtils.setTranslateAnimation(holder.mEtMinNum, holder.mLlMinLeft, holder.mRlMin, holder.mIvMinPlus);

        final ViewHolder holder1 = holder;

        holder.mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClearNum(i, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                AnimUtils.setTranslateAnimation(holder1.mEtMaxNum, holder1.mLlMaxLeft, holder1.mRlMax, holder1.mIvMaxPlus);
                AnimUtils.setTranslateAnimation(holder1.mEtMinNum, holder1.mLlMinLeft, holder1.mRlMin, holder1.mIvMinPlus);
            }
        });

        holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxMinus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxPlus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinMinus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinPlus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
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

        holder.mEtDisplayNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    mEtDisplayNum = holder2.mEtDisplayNum;
                    holder2.mEtDisplayNum.addTextChangedListener(textDisplayWatcher);
                } else {
                    holder2.mEtDisplayNum.removeTextChangedListener(textDisplayWatcher);
                }
            }
        });

        holder.mEtDuitouNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    mEtDuitouNum = holder2.mEtDuitouNum;
                    holder2.mEtDuitouNum.addTextChangedListener(textDuitouWatcher);
                } else {
                    holder2.mEtDuitouNum.removeTextChangedListener(textDuitouWatcher);
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
            listener.onMaxEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum, tvSumPrice);
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
            listener.onMinEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum, tvSumPrice);
        }
    };

    private TextWatcher textDisplayWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onDiaplayNum(index, editable.toString());
        }
    };

    private TextWatcher textDuitouWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onDuitouNum(index, editable.toString());
        }
    };

    public interface InventoryListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onDiaplayNum(int i, String num);

        void onDuitouNum(int i, String num);
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.iv_max_minus)
        ImageView mIvMaxMinus;
        @BindView(R.id.et_max_num)
        EditText mEtMaxNum;
        @BindView(R.id.ll_max_left)
        LinearLayout mLlMaxLeft;
        @BindView(R.id.iv_max_plus)
        ImageView mIvMaxPlus;
        @BindView(R.id.rl_max)
        RelativeLayout mRlMax;
        @BindView(R.id.tv_max_unit_name)
        TextView mTvMaxUnitName;
        @BindView(R.id.iv_min_minus)
        ImageView mIvMinMinus;
        @BindView(R.id.et_min_num)
        EditText mEtMinNum;
        @BindView(R.id.ll_min_left)
        LinearLayout mLlMinLeft;
        @BindView(R.id.iv_min_plus)
        ImageView mIvMinPlus;
        @BindView(R.id.rl_min)
        RelativeLayout mRlMin;
        @BindView(R.id.tv_min_unit_name)
        TextView mTvMinUnitName;
        @BindView(R.id.tv_total_price)
        TextView mTvTotalPrice;
        @BindView(R.id.tv_clear)
        TextView mTvClear;
        @BindView(R.id.ll_max_main)
        LinearLayout mLlMaxMain;
        @BindView(R.id.tv_inventory_date)
        TextView mTvInventoryDate;
        @BindView(R.id.et_display_num)
        EditText mEtDisplayNum;
        @BindView(R.id.et_duitou_num)
        EditText mEtDuitouNum;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
