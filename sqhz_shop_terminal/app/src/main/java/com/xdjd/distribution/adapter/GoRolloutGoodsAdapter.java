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
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.LogUtils;

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

public class GoRolloutGoodsAdapter extends BaseAdapter {

    public List<GoodsBean> list;
    private GoRolloutGoodsListener listener;
    private int index;
    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private TextView tvSumPrice;

    public GoRolloutGoodsAdapter(GoRolloutGoodsListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_go_rollout_goods_cart, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvName.setText(list.get(i).getGg_title());

        if (holder.mEtMaxNum.hasFocus()) {
            holder.mEtMaxNum.setFocusable(false);
            holder.mEtMaxNum.setFocusable(true);
            holder.mEtMaxNum.setFocusableInTouchMode(true);
            //            holder.mEtMaxNum.requestFocus();
            //            holder.mEtMaxNum.findFocus();
        }
        if (holder.mEtMinNum.hasFocus()) {
            holder.mEtMinNum.setFocusable(false);
            holder.mEtMinNum.setFocusable(true);
            holder.mEtMinNum.setFocusableInTouchMode(true);
        }
        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());

        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(list.get(i).getGgp_unit_num())) {
            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mLlMax.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(list.get(i).getMin_price());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        } else {
            holder.mLlMaxMain.setVisibility(View.VISIBLE);
            holder.mLlMax.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(list.get(i).getMax_price());//大单位价格
            holder.mTvMinPrice.setText(list.get(i).getMin_price());//小单位价格
            holder.mTvMaxUnit.setText("元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        }

        //        holder.mTvTotalPrice.setText(list.get(i).getTotalPrice());
        UnitCalculateUtils.setTranslateAnimation(holder.mEtMaxNum, holder.mLlMaxLeft, holder.mRlMax, holder.mIvMaxPlus);
        UnitCalculateUtils.setTranslateAnimation(holder.mEtMinNum, holder.mLlMinLeft, holder.mRlMin, holder.mIvMinPlus);


       /* if (!list.get(i).getMinNum().equals("0") && !list.get(i).getMinNum().equals("")){
            holder.mLlMinLeft.setVisibility(View.VISIBLE);
        }else{
            holder.mLlMinLeft.setVisibility(View.GONE);
            LogUtils.e("小单位","隐藏了");
        }
        if (!list.get(i).getMaxNum().equals("0") && !list.get(i).getMaxNum().equals("")){
            holder.mLlMaxLeft.setVisibility(View.VISIBLE);
        }else{
            holder.mLlMaxLeft.setVisibility(View.GONE);
        }*/

        String stockStr = UnitCalculateUtils.unitStr(list.get(i).getGgp_unit_num(),list.get(i).getGgs_stock(),
                list.get(i).getGg_unit_max_nameref(),list.get(i).getGg_unit_min_nameref());
        holder.mTvStock.setText(stockStr);

        holder.mTvMaxUnitName.setText(list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnitName.setText(list.get(i).getGg_unit_min_nameref());
       /* holder.mRlAddCartnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onShowEditNumAlter(holder.mItemShowAddnum);
            }
        });
*/
        final ViewHolder holder1 = holder;
        holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxMinus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxPlus(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinMinus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinPlus(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
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
                    holder2.mEtMinNum.addTextChangedListener(textMinWatcher);
                } else {
                    holder2.mEtMinNum.removeTextChangedListener(textMinWatcher);
                }
            }
        });

        /*if (list.get(i).getOa_id() != null && list.get(i).getOa_id().length() > 0){//还货商品
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
                holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model());
            }
        }
        */

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
            listener.onMaxEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum);
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
            listener.onMinEtNum(index, editable.toString(), mEtMaxNum, mEtMinNum);
        }
    };

    public interface GoRolloutGoodsListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum/*, TextView tvSumPrice*/);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum);

        /*void onShowEditNumAlter(LinearLayout itemShowAddnum);*/

        /*void onShowEditPriceAlter(LinearLayout itemShowEditPrice);

        void onEditMaxPrice(EditText mEtMaxPrice, EditText mEtMinPrice);

        void onEditMinPrice(EditText mEtMaxPrice, EditText mEtMinPrice);*/
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;
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
        @BindView(R.id.tv_stock)
        TextView mTvStock;
        @BindView(R.id.tv_max_price)
        TextView mTvMaxPrice;
        @BindView(R.id.tv_min_price)
        TextView mTvMinPrice;
        @BindView(R.id.ll_max)
        LinearLayout mLlMax;
        @BindView(R.id.ll_min)
        LinearLayout mLlMin;
        //        @BindView(R.id.rl_add_cartnum)
        //        RelativeLayout mRlAddCartnum;
        //        @BindView(R.id.item_show_addnum)
        //        LinearLayout mItemShowAddnum;
       /* @BindView(R.id.item_show_editPrice)
        LinearLayout mItemShowEditPrice;
        @BindView(R.id.et_max_price)
        LastInputEditText mEtMaxPrice;
        @BindView(R.id.et_min_price)
        LastInputEditText mEtMinPrice;*/

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
