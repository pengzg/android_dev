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
import com.xdjd.view.LastInputEditText;

import java.math.BigDecimal;
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

public class DisplayCartAdapter extends BaseAdapter {

    public List<GoodsBean> list;
    private GoRolloutGoodsListener listener;
    private int index;
    private EditText mEtMaxNum, mEtMinNum;
    private LastInputEditText mEtMaxPrice, mEtMinPrice;
    private TextView mTvTotalPrice, mTvMaxPrice, mTvMinPrice;

    public DisplayCartAdapter(GoRolloutGoodsListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_display_goods_cart, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //商品名称
        holder.mTvName.setText(list.get(i).getGg_title());
        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(list.get(i).getGgp_unit_num())) {
            holder.mLlMax.setVisibility(View.INVISIBLE);

            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(list.get(i).getMinPrice());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        } else {
            holder.mLlMax.setVisibility(View.VISIBLE);

            holder.mLlMaxMain.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(list.get(i).getMaxPrice());//大单位价格
            holder.mTvMinPrice.setText(list.get(i).getMinPrice());//小单位价格

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

    public interface GoRolloutGoodsListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);
    }

    public class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_max_price)
        TextView mTvMaxPrice;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.ll_max)
        LinearLayout mLlMax;
        @BindView(R.id.tv_min_price)
        TextView mTvMinPrice;
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

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
