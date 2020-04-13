package com.xdjd.distribution.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.base.UnitCalculateUtils;
import com.xdjd.distribution.bean.PHOrderDetailBean;
import com.xdjd.utils.AnimUtils;
import com.xdjd.view.AnimatedExpandableListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/12/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class RolloutGoodsSellCartAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    public int index = 0;
    public int index2 = 0;
    List<PHOrderDetailBean> list;
    private OnChildListerer listenerChild;
    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private String beforeMaxNum, beforeMinNum;

    public void setOnChildListerer(OnChildListerer listenerChild) {
        this.listenerChild = listenerChild;
    }

    public void setData(List<PHOrderDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getRealChildrenCount(int i) {
        return list.get(i) == null ? 0 : list.get(i).getListGoodsData().size();
    }


    @Override
    public PHOrderDetailBean getGroup(int i) {
        return list.get(i);
    }

    //获取子列表项对应的Item
    @Override
    public PHOrderDetailBean getChild(int i, int i1) {
        return list.get(i).getListGoodsData().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    //获得子列表项的Id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_rollout_goods_edit_cart_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PHOrderDetailBean bean = list.get(i);

        holder.mTvIndex.setText((i + 1) + "");
        holder.mTvName.setText(bean.getOrderCode());
        holder.mTvPlaceOrderTime.setText("下单时间:" + bean.getAddTime());
        holder.mTvTotalAmount.setText(bean.getTotalAmount());
        //        holder.mTvWsAmount.setText(bean.);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_total_amount)
        TextView mTvTotalAmount;
        @BindView(R.id.tv_place_order_time)
        TextView mTvPlaceOrderTime;
        @BindView(R.id.line)
        View mLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getRealChildView(final int i, final int i1, boolean b, View view, ViewGroup parent) {
        ViewHolderChild holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_rollout_goods_edit_cart_child, parent, false);
            holder = new ViewHolderChild(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolderChild) view.getTag();
        }

        PHOrderDetailBean bean = list.get(i).getListGoodsData().get(i1);

        holder.mTvName.setText(bean.getGoodsName());
        holder.mTvTotalNum.setText("总数量:" + bean.getPhTotalNum_desc());
        holder.mTvSellNum.setText("已销售:" + bean.getPhSaleNum_desc());
        holder.mTvNoSellNum.setText("未销售:" + bean.getResidueNum_desc());

        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(bean.getUnitNum())) {
            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mLlMax.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(bean.getMinPrice());
            holder.mTvMinUnit.setText("元/" + bean.getMinName());
            holder.mTvMinUnitName.setText(bean.getMinName());
        } else {
            holder.mLlMaxMain.setVisibility(View.VISIBLE);
            holder.mLlMax.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(bean.getMaxPrice());//大单位价格
            holder.mTvMinPrice.setText(bean.getMinPrice());//小单位价格
            holder.mTvMaxUnit.setText("元/" + bean.getMaxName());
            holder.mTvMinUnit.setText("元/" + bean.getMinName());
            holder.mTvMaxUnitName.setText(bean.getMaxName());
            holder.mTvMinUnitName.setText(bean.getMinName());
        }

        if (holder.mEtMaxNum.hasFocus()) {
            holder.mEtMaxNum.setFocusable(false);
            holder.mEtMaxNum.setFocusable(true);
            holder.mEtMaxNum.setFocusableInTouchMode(true);
        }

        if (holder.mEtMinNum.hasFocus()) {
            holder.mEtMinNum.setFocusable(false);
            holder.mEtMinNum.setFocusable(true);
            holder.mEtMinNum.setFocusableInTouchMode(true);
        }

        holder.mEtMaxNum.setText(bean.getMaxNum());
        holder.mEtMinNum.setText(bean.getMinNum());

        //        holder.mTvTotalPrice.setText(bean.getTotalPrice());

        UnitCalculateUtils.setTranslateAnimation(holder.mEtMaxNum, holder.mLlMaxLeft, holder.mRlMax, holder.mIvMaxPlus);
        UnitCalculateUtils.setTranslateAnimation(holder.mEtMinNum, holder.mLlMinLeft, holder.mRlMin, holder.mIvMinPlus);

        final ViewHolderChild holder1 = holder;
        holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMaxNum.hasFocus()){
                    holder1.mEtMaxNum.clearFocus();
                }
                listenerChild.onMaxMinus(i, i1, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMaxNum.hasFocus()){
                    holder1.mEtMaxNum.clearFocus();
                }
                listenerChild.onMaxPlus(i, i1, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMinNum.hasFocus()){
                    holder1.mEtMinNum.clearFocus();
                }
                listenerChild.onMinMinus(i, i1, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.mEtMinNum.hasFocus()){
                    holder1.mEtMinNum.clearFocus();
                }
                listenerChild.onMinPlus(i, i1, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum);
            }
        });

        final ViewHolderChild holder2 = holder;
        holder.mEtMaxNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    index = i;
                    index2 = i1;
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
                    index2 = i1;
                    mEtMaxNum = holder2.mEtMaxNum;
                    mEtMinNum = holder2.mEtMinNum;
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
            beforeMaxNum = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listenerChild.onMaxEtNum(index, index2, editable.toString(), mEtMaxNum, mEtMinNum, beforeMaxNum);
        }
    };

    private TextWatcher textMinWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            beforeMinNum = charSequence.toString();
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            listenerChild.onMinEtNum(index, index2, editable.toString(), mEtMaxNum, mEtMinNum, beforeMinNum);
        }
    };

    public interface OnChildListerer {

        void onMaxPlus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum/*, TextView tvSumPrice*/);

        void onMaxMinus(int i, int i1, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum);

        void onMinPlus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum);

        void onMinMinus(int i, int i1, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum);

        void onMaxEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeNum);

        void onMinEtNum(int i, int i1, String num, EditText mEtMaxNum, EditText mEtMinNum, String beforeNum);
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class ViewHolderChild {
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
        //        @BindView(R.id.close)
        //        LinearLayout mClose;
        //        @BindView(R.id.item_show_addnum)
        //        LinearLayout mItemShowAddnum;
        @BindView(R.id.tv_total_num)
        TextView mTvTotalNum;
        @BindView(R.id.tv_sell_num)
        TextView mTvSellNum;
        @BindView(R.id.tv_no_sell_num)
        TextView mTvNoSellNum;
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

        ViewHolderChild(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
