package com.xdjd.distribution.adapter;

import android.text.Editable;
import android.text.TextUtils;
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
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.AnimUtils;
import com.xdjd.view.LastInputEditText;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/11
 *     desc   : 去铺货商品adapter
 *     version: 1.0
 * </pre>
 */

public class GoRolloutGoodsCartAdapter extends BaseAdapter {

    public List<GoodsBean> list;
    private GoRolloutGoodsListener listener;
    private int index;
    private EditText mEtMaxNum, mEtMinNum;
    private LastInputEditText mEtMaxPrice, mEtMinPrice;
    private TextView mTvTotalPrice, mTvMaxPrice, mTvMinPrice;

    public GoRolloutGoodsCartAdapter(GoRolloutGoodsListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_go_rollout_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mLlMaxPrice.setVisibility(View.GONE);
        holder.mLlMinPrice.setVisibility(View.GONE);
        holder.mTvMaxPrice.setVisibility(View.VISIBLE);
        holder.mTvMinPrice.setVisibility(View.VISIBLE);
        holder.mIvEditPrice.setSelected(false);

        //商品名称
        holder.mTvName.setText(list.get(i).getGg_title());
        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(list.get(i).getGgp_unit_num())) {
            holder.mLlMax.setVisibility(View.INVISIBLE);

            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(list.get(i).getMinPrice());
            holder.mEtMinPrice.setText(list.get(i).getMinPrice());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        } else {
            holder.mLlMax.setVisibility(View.VISIBLE);

            holder.mLlMaxMain.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(list.get(i).getMaxPrice());//大单位价格
            holder.mTvMinPrice.setText(list.get(i).getMinPrice());//小单位价格
            holder.mEtMaxPrice.setText(list.get(i).getMaxPrice());
            holder.mEtMinPrice.setText(list.get(i).getMinPrice());

            holder.mTvMaxUnit.setText("元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        }

        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());
        holder.mTvTotalPrice.setText(list.get(i).getTotalPrice());
        //大小单位判断隐藏动画
        UnitCalculateUtils.setTranslateAnimation(holder.mEtMaxNum, holder.mLlMaxLeft, holder.mRlMax, holder.mIvMaxPlus);
        UnitCalculateUtils.setTranslateAnimation(holder.mEtMinNum, holder.mLlMinLeft, holder.mRlMin, holder.mIvMinPlus);
        //库存数量
        holder.mTvStock.setText(UnitCalculateUtils.unitStr(list.get(i).getGgp_unit_num(), list.get(i).getGgs_stock(),
                list.get(i).getGg_unit_max_nameref(), list.get(i).getGg_unit_min_nameref()));
        //大小单位显示
        holder.mTvMaxUnitName.setText(list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnitName.setText(list.get(i).getGg_unit_min_nameref());

        final ViewHolder holder1 = holder;
        holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMaxNum.hasFocus()){
                    holder1.mEtMaxNum.clearFocus();
                }
                listener.onMaxMinus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMaxNum.hasFocus()){
                    holder1.mEtMaxNum.clearFocus();
                }
                listener.onMaxPlus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMinNum.hasFocus()){
                    holder1.mEtMinNum.clearFocus();
                }
                listener.onMinMinus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum,holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMinNum.hasFocus()){
                    holder1.mEtMinNum.clearFocus();
                }
                listener.onMinPlus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum,holder1.mTvTotalPrice);
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
                    mTvTotalPrice = holder2.mTvTotalPrice;
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
                    mTvTotalPrice = holder2.mTvTotalPrice;
                    holder2.mEtMinNum.addTextChangedListener(textMinWatcher);
                } else {
                    holder2.mEtMinNum.removeTextChangedListener(textMinWatcher);
                }
            }
        });

        holder.mLlEditPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder2.mIvEditPrice.isSelected()) {
                    holder2.mIvEditPrice.setSelected(true);

                    BigDecimal bdMaxPrice,bdMinPrice;
                    if (TextUtils.isEmpty(holder2.mEtMaxPrice.getText())){
                        bdMaxPrice = BigDecimal.ZERO;
                    } else {
                        bdMaxPrice = new BigDecimal(holder2.mEtMaxPrice.getText().toString());
                    }
                    if (TextUtils.isEmpty(holder2.mEtMinPrice.getText())){
                        bdMinPrice = BigDecimal.ZERO;
                    } else {
                        bdMinPrice = new BigDecimal(holder2.mEtMinPrice.getText().toString());
                    }

                    if (new BigDecimal(bdMaxPrice.intValue()).compareTo(bdMaxPrice) == 0){
                        //是整数
                        holder2.mEtMaxPrice.setText(bdMaxPrice.intValue()+"");
                    }
                    if (new BigDecimal(bdMinPrice.intValue()).compareTo(bdMinPrice) == 0){
                        //是整数
                        holder2.mEtMinPrice.setText(bdMinPrice.intValue()+"");
                    }

                    holder2.mLlMinPrice.setVisibility(View.VISIBLE);
                    holder2.mLlMaxPrice.setVisibility(View.VISIBLE);
                    holder2.mTvMaxPrice.setVisibility(View.GONE);
                    holder2.mTvMinPrice.setVisibility(View.GONE);
                } else {
                    holder2.mIvEditPrice.setSelected(false);
                    holder2.mLlMinPrice.setVisibility(View.GONE);
                    holder2.mLlMaxPrice.setVisibility(View.GONE);
                    holder2.mTvMaxPrice.setVisibility(View.VISIBLE);
                    holder2.mTvMinPrice.setVisibility(View.VISIBLE);

                    if ("".equals(holder2.mEtMaxPrice.getText().toString())){
                        holder2.mTvMaxPrice.setText("0.00");
                    }
                    if ("".equals(holder2.mEtMinPrice.getText().toString())){
                        holder2.mTvMinPrice.setText("0.00");
                    }
                }
            }
        });

        holder.mEtMaxPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    index = i;
                    mEtMaxPrice = holder2.mEtMaxPrice;
                    mEtMinPrice = holder2.mEtMinPrice;
                    mTvTotalPrice = holder2.mTvTotalPrice;
                    mTvMaxPrice = holder2.mTvMaxPrice;
                    mTvMinPrice = holder2.mTvMinPrice;
                    holder2.mEtMaxPrice.addTextChangedListener(textMaxPriceWatcher);
                } else {
                    holder2.mEtMaxPrice.removeTextChangedListener(textMaxPriceWatcher);
                }
            }
        });

        holder.mEtMinPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    index = i;
                    mEtMaxPrice = holder2.mEtMaxPrice;
                    mEtMinPrice = holder2.mEtMinPrice;
                    mTvTotalPrice = holder2.mTvTotalPrice;
                    mTvMaxPrice = holder2.mTvMaxPrice;
                    mTvMinPrice = holder2.mTvMinPrice;
                    holder2.mEtMinPrice.addTextChangedListener(textMinPriceWatcher);
                } else {
                    holder2.mEtMinPrice.removeTextChangedListener(textMinPriceWatcher);
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
            listener.onMaxEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum,mTvTotalPrice);
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
            listener.onMinEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum,mTvTotalPrice);
        }
    };

    private TextWatcher textMaxPriceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtMaxPrice.setText(s);
                    mEtMaxPrice.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtMaxPrice.setText(s);
                mEtMaxPrice.setSelection(2);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onMaxPrice(index, editable.toString(), mEtMaxPrice, mEtMinPrice, mTvTotalPrice, mTvMaxPrice, mTvMinPrice);
        }
    };

    private TextWatcher textMinPriceWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            if (s.toString().contains(".")) {
                if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                    mEtMinPrice.setText(s);
                    mEtMinPrice.setSelection(s.length());
                }
            }
            if (s.toString().trim().substring(0).equals(".")) {
                s = "0" + s;
                mEtMinPrice.setText(s);
                mEtMinPrice.setSelection(2);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listener.onMinPrice(index, editable.toString(), mEtMaxPrice, mEtMinPrice, mTvTotalPrice, mTvMaxPrice, mTvMinPrice);
        }
    };

    public interface GoRolloutGoodsListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxPrice(int i, String editable, EditText mEtMaxPrice, EditText mEtMinPrice, TextView tvSumPrice, TextView mTvMaxPrice, TextView mTvMinPrice);

        void onMinPrice(int i, String editable, EditText mEtMaxPrice, EditText mEtMinPrice, TextView tvSumPrice, TextView mTvMaxPrice, TextView mTvMinPrice);
    }

    public class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_max_price)
        TextView mTvMaxPrice;
        @BindView(R.id.et_max_price)
        LastInputEditText mEtMaxPrice;
        @BindView(R.id.ll_max_price)
        LinearLayout mLlMaxPrice;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.ll_max)
        LinearLayout mLlMax;
        @BindView(R.id.tv_min_price)
        TextView mTvMinPrice;
        @BindView(R.id.et_min_price)
        LastInputEditText mEtMinPrice;
        @BindView(R.id.ll_min_price)
        LinearLayout mLlMinPrice;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;
        @BindView(R.id.ll_min)
        LinearLayout mLlMin;
        @BindView(R.id.tv_stock)
        TextView mTvStock;
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
        @BindView(R.id.ll_max_main)
        LinearLayout mLlMaxMain;
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
        @BindView(R.id.ll_edit_price)
        LinearLayout mLlEditPrice;
        @BindView(R.id.iv_edit_price)
        ImageView mIvEditPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
