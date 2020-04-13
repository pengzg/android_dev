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
import com.xdjd.distribution.base.Comon;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.AnimUtils;

import java.math.BigDecimal;
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

public class OrderDeclareGoodsAdapter extends BaseAdapter {

    public List<GoodsBean> list;

    private OrderDeclareGoodsListener listener;

    private int index;

    private RelativeLayout mRlMax;
    private LinearLayout mLlMaxLeft;
    private ImageView mIvMax;

    private RelativeLayout mRlMin;
    private LinearLayout mLlMinLeft;
    private ImageView mIvMin;

    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private TextView tvSumPrice;

    public OrderDeclareGoodsAdapter(OrderDeclareGoodsListener listener) {
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_declare_goods_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvIndex.setText(String.valueOf(i + 1));

        if (list.get(i).getOa_id() != null && list.get(i).getOa_id().length() > 0){//还货商品
            BigDecimal unitNum = new BigDecimal(list.get(i).getGgp_unit_num());

            if ("1".equals(list.get(i).getGgp_unit_num())) {
                holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model()+
                        "---剩余" + list.get(i).getOrder_surplus_num() + list.get(i).getGg_unit_min_nameref());
            } else {
                BigDecimal surplusNum = new BigDecimal(list.get(i).getOrder_surplus_num());
                BigDecimal[] results = surplusNum.divideAndRemainder(unitNum);

                holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model()+
                        "---剩余" + results[0] + list.get(i).getGg_unit_max_nameref() + results[1] + list.get(i).getGg_unit_min_nameref());
            }
        }else{
            if (list.get(i).getGoodsStatus() == 0 || list.get(i).getGoodsStatus() == 1){
                holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model());
            }else{
                String goodsStatusStr;
                switch (list.get(i).getGoodsStatus()) {//商品状态 1 正 2 临 3残 4过
                    case Comon.GOODS_STATUS:
                        goodsStatusStr = "[正]";
                        break;
                    case Comon.GOODS_STATUS_L:
                        goodsStatusStr = "[临]";
                        break;
                    case Comon.GOODS_STATUS_C:
                        goodsStatusStr = "[残]";
                        break;
                    case Comon.GOODS_STATUS_G:
                        goodsStatusStr = "[过]";
                        break;
                    default:
                        goodsStatusStr = "";
                        break;
                }
                holder.mTvName.setText(goodsStatusStr + list.get(i).getGg_title() + " " + list.get(i).getGg_model());
            }
        }

        holder.mTvStock.setText(list.get(i).getStock_nameref());

        holder.mTvMaxUnitName.setText(list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnitName.setText(list.get(i).getGg_unit_min_nameref());

        //如果大小单位换算比==1隐藏大单位
        if (list.get(i).getGgp_unit_num().equals("1")){
            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinUnit.setText(list.get(i).getMinPrice() + "元/" + list.get(i).getGg_unit_min_nameref());
        }else{
            holder.mLlMaxMain.setVisibility(View.VISIBLE);

            holder.mTvMaxUnit.setText(list.get(i).getMaxPrice() + "元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText(list.get(i).getMinPrice() + "元/" + list.get(i).getGg_unit_min_nameref());
        }
        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());

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

                    mRlMax = holder2.mRlMax;
                    mLlMaxLeft = holder2.mLlMaxLeft;
                    mIvMax = holder2.mIvMaxPlus;

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

                    mRlMin = holder2.mRlMin;
                    mLlMinLeft = holder2.mLlMinLeft;
                    mIvMin = holder2.mIvMinPlus;

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
            listener.onMaxEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum, tvSumPrice);
            mEtMaxNum.setSelection(editable.length());
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
            mEtMinNum.setSelection(editable.length());
        }
    };

    public interface OrderDeclareGoodsListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onClearNum(int i, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);
    }

    public class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
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
        @BindView(R.id.tv_max_unit_name)
        TextView mTvMaxUnitName;
        @BindView(R.id.tv_min_unit_name)
        TextView mTvMinUnitName;
        @BindView(R.id.iv_max_minus)
        ImageView mIvMaxMinus;
        @BindView(R.id.ll_max_left)
        LinearLayout mLlMaxLeft;
        @BindView(R.id.iv_max_plus)
        ImageView mIvMaxPlus;
        @BindView(R.id.rl_max)
        RelativeLayout mRlMax;
        @BindView(R.id.iv_min_minus)
        ImageView mIvMinMinus;
        @BindView(R.id.ll_min_left)
        LinearLayout mLlMinLeft;
        @BindView(R.id.iv_min_plus)
        ImageView mIvMinPlus;
        @BindView(R.id.rl_min)
        RelativeLayout mRlMin;
        @BindView(R.id.ll_max_main)
        LinearLayout mLlMaxMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
