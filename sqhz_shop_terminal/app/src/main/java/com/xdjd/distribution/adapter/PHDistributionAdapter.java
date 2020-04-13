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
import com.xdjd.distribution.bean.DistributionGoodsBean;
import com.xdjd.distribution.bean.OrderBean;
import com.xdjd.distribution.bean.PHTaskBean;
import com.xdjd.distribution.bean.UserBean;
import com.xdjd.utils.AnimUtils;
import com.xdjd.utils.DialogUtil;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.NoScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/6/6
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class PHDistributionAdapter extends BaseAdapter {

    public List<PHTaskBean> list;

    private PHDistributionListener listener;

    private UserBean userBean;

    public PHDistributionAdapter(PHDistributionListener listener, UserBean userBean) {
        this.listener = listener;
        this.userBean = userBean;
    }

    public void setData(List<PHTaskBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public PHTaskBean getItem(int i) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ph_distribution_task_order, viewGroup, false);
            holder = new ViewHolder(view);
            holder.adapterChild = new PHDistributionChildAdapter();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvOrderAmount.setText("合计 ¥:" + list.get(i).getOa_order_amount());
        holder.mTvSalesmanName.setText(list.get(i).getOa_customerid_nameref());
        holder.mTvOrderId.setText(list.get(i).getOa_applycode());

        holder.mLvOrderGoods.setAdapter(holder.adapterChild);
        holder.adapterChild.setData(i,list.get(i).getDetailList());

        holder.mTvGoodsNum.setText("共" + list.get(i).getDetailList().size() + "件");

        holder.mTvOrderClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clearZero(i);
            }
        });

        holder.mTvNoDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.noDistribution(i);
            }
        });

        holder.mTvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSubmitOrder(i);
            }
        });

        holder.mTvOrderRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOrderRejected(i);
            }
        });

        final ViewHolder holder1 = holder;

        holder.adapterChild.setListener(new PHDistributionChildListener() {
            @Override
            public void onMaxPriceChild(int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMaxPriceChild(i, k, mEtMaxPrice, mEtMinPrice, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMinPriceChild(int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMinPriceChild(i, k, mEtMaxPrice, mEtMinPrice, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMaxPlusChild(int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMaxPlus(i, k, mRlMax, mLlMaxLeft, mIvMax, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMaxMinusChild(int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMaxMinus(i, k, mRlMax, mLlMaxLeft, mIvMax, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMinPlusChild(int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMinPlus(i, k, mRlMin, mLlMinLeft, mIvMin, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMinMinusChild(int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMinMinus(i, k, mRlMin, mLlMinLeft, mIvMin, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onClearNumChild(int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onClearNum(i, k, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMaxEtNumChild(int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMaxEtNum(i, k, num, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }

            @Override
            public void onMinEtNumChild(int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice) {
                listener.onMinEtNum(i, k, num, mEtMaxNum, mEtMinNum, tvSumPrice, holder1.mTvOrderAmount);
            }
        });

        return view;
    }

    public interface PHDistributionListener {
        void noDistribution(int i);//暂不配送

        void clearZero(int i);//订单清零

        void onSubmitOrder(int i);//订单提交

        void onOrderRejected(int i);//订单拒收

        void onMaxPlus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onMaxMinus(int i, int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onMinPlus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onMinMinus(int i, int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onClearNum(int i, int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onMaxEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        void onMinEtNum(int i, int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice, TextView mTvOrderAmount);

        //---------------
        void onMaxPriceChild(int i, int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum,
                             TextView tvSumPrice, TextView mTvOrderAmount);

        void onMinPriceChild(int i, int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum,
                             TextView tvSumPrice, TextView mTvOrderAmount);
    }

    class ViewHolder {
        @BindView(R.id.tv_order_id)
        TextView mTvOrderId;
        @BindView(R.id.tv_salesman_name)
        TextView mTvSalesmanName;
        @BindView(R.id.tv_order_amount)
        TextView mTvOrderAmount;
        @BindView(R.id.tv_order_clear)
        TextView mTvOrderClear;
        @BindView(R.id.lv_order_goods)
        NoScrollListView mLvOrderGoods;
        @BindView(R.id.tv_no_distribution)
        TextView mTvNoDistribution;
        @BindView(R.id.tv_submit)
        TextView mTvSubmit;
        @BindView(R.id.tv_goods_num)
        TextView mTvGoodsNum;
        @BindView(R.id.tv_order_rejected)
        TextView mTvOrderRejected;

        PHDistributionChildAdapter adapterChild;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public class PHDistributionChildAdapter extends BaseAdapter {

        public List<PHTaskBean> listChild;

        private PHDistributionChildListener listener;

        private int index;

        private EditText mEtMaxNum;
        private EditText mEtMinNum;
        private TextView tvSumPrice;

        private int groupIndex;

        public void setListener(PHDistributionChildListener listener) {
            this.listener = listener;
        }

        public void setData(int groupIndex,List<PHTaskBean> listChild) {
            this.listChild = listChild;
            this.groupIndex = groupIndex;
            notifyDataSetInvalidated();
        }

        @Override
        public int getCount() {
            return listChild == null ? 0 : listChild.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).
                        inflate(R.layout.item_ph_distribution_order_goods, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mTvIndex.setText(String.valueOf(i + 1));

            holder.mTvName.setText(listChild.get(i).getOad_goods_name() + " " + listChild.get(i).getGg_model());
            //            holder.mTvStock.setText(listChild.get(i).getOd_stock());//暂时用的是自定义参数


            /*if ("1".equals(list.get(groupIndex).getFlag())){//	是否允许修改数量 1 允许 2 不允许
                if ("2".equals(listChild.get(i).getOad_goodstype())) {//getOd_goodstype商品类型 1 普通 2 赠品
                    holder.mEtMaxPrice.setEnabled(false);
                    holder.mEtMinPrice.setEnabled(false);

                    holder.mEtMaxNum.setEnabled(false);
                    holder.mEtMinNum.setEnabled(false);

                    holder.mEtMaxPrice.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                    holder.mEtMinPrice.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                    holder.mEtMaxNum.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));
                    holder.mEtMinNum.setTextColor(UIUtils.getColor(R.color.text_gray_b3b3));

                    holder.mIvMaxMinus.setVisibility(View.INVISIBLE);
                    holder.mIvMaxPlus.setVisibility(View.INVISIBLE);
                    holder.mIvMinMinus.setVisibility(View.INVISIBLE);
                    holder.mIvMinPlus.setVisibility(View.INVISIBLE);

                    holder.mTvClear.setVisibility(View.INVISIBLE);
                } else {
                    holder.mEtMaxPrice.setTextColor(UIUtils.getColor(R.color.text_blue));
                    holder.mEtMinPrice.setTextColor(UIUtils.getColor(R.color.text_blue));
                    holder.mEtMaxNum.setTextColor(UIUtils.getColor(R.color.text_blue));
                    holder.mEtMinNum.setTextColor(UIUtils.getColor(R.color.text_blue));

                    holder.mIvMaxMinus.setVisibility(View.VISIBLE);
                    holder.mIvMaxPlus.setVisibility(View.VISIBLE);
                    holder.mIvMinMinus.setVisibility(View.VISIBLE);
                    holder.mIvMinPlus.setVisibility(View.VISIBLE);

                    holder.mTvClear.setVisibility(View.VISIBLE);

                    holder.mEtMaxNum.setEnabled(true);
                    holder.mEtMinNum.setEnabled(true);

                    //                if (userBean.getIsChangPrice().equals("2")) {
                    holder.mEtMaxPrice.setEnabled(false);
                    holder.mEtMinPrice.setEnabled(false);
                    //                } else {
                    //                    holder.mEtMaxPrice.setEnabled(true);
                    //                    holder.mEtMinPrice.setEnabled(true);
                    //                }
                }
            }else{*/
                holder.mEtMaxNum.setEnabled(false);
                holder.mEtMinNum.setEnabled(false);
//            }

            holder.mTvMaxUnitName.setText(listChild.get(i).getOad_goods_unitname_max());
            holder.mTvMinUnitName.setText(listChild.get(i).getOad_goods_unitname_min());

            holder.mTvMaxPrice.setText(listChild.get(i).getOad_price_max());
            holder.mTvMinPrice.setText(listChild.get(i).getOad_price_min());

            //如果大小单位换算比==1,隐藏大单位
            if ("1".equals(listChild.get(i).getOad_unit_num())) {
                holder.mLlMaxMain.setVisibility(View.INVISIBLE);
                holder.mLlMaxPrice.setVisibility(View.INVISIBLE);

                holder.mTvMaxUnit.setText(listChild.get(i).getOad_goods_unitname_max());
                holder.mTvMinUnit.setText(/*listChild.get(i).getOd_price_min() + "元/" +*/ listChild.get(i).getOad_goods_unitname_min());
            } else {
                holder.mLlMaxMain.setVisibility(View.VISIBLE);
                holder.mLlMaxPrice.setVisibility(View.VISIBLE);

                holder.mTvMaxUnit.setText(/*listChild.get(i).getOd_price_max() + "元/" +*/ "元/" +listChild.get(i).getOad_goods_unitname_max());
                holder.mTvMinUnit.setText(/*listChild.get(i).getOd_price_min() + "元/" +*/ "元/" +listChild.get(i).getOad_goods_unitname_min());
            }

            holder.mEtMaxNum.setText(listChild.get(i).getOad_goods_num_max());
            holder.mEtMinNum.setText(listChild.get(i).getOad_goods_num_min());

            holder.mTvTotalPrice.setText(listChild.get(i).getOad_real_total());

            AnimUtils.setTranslateAnimation(holder.mEtMaxNum, holder.mLlMaxLeft, holder.mRlMax, holder.mIvMaxPlus);
            AnimUtils.setTranslateAnimation(holder.mEtMinNum, holder.mLlMinLeft, holder.mRlMin, holder.mIvMinPlus);

            final ViewHolder holder1 = holder;

            holder.mTvClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(listChild.get(i).getOad_goodstype()))
                        return;
//                    if ("2".equals(list.get(groupIndex).getFlag())){//是否允许修改数量 1 允许 2 不允许
//                        DialogUtil.showCustomDialog(viewGroup.getContext(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
//                        return;
//                    }
                    listener.onClearNumChild(i, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                    AnimUtils.setTranslateAnimation(holder1.mEtMaxNum, holder1.mLlMaxLeft, holder1.mRlMax, holder1.mIvMaxPlus);
                    AnimUtils.setTranslateAnimation(holder1.mEtMinNum, holder1.mLlMinLeft, holder1.mRlMin, holder1.mIvMinPlus);
                }
            });

            holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(listChild.get(i).getOad_goodstype()))
                        return;
//                    if ("2".equals(list.get(groupIndex).getFlag())){//是否允许修改数量 1 允许 2 不允许
//                        DialogUtil.showCustomDialog(viewGroup.getContext(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
//                        return;
//                    }
                    listener.onMaxMinusChild(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                }
            });

            holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(listChild.get(i).getOad_goodstype()))
                        return;
//                    if ("2".equals(list.get(groupIndex).getFlag())){//是否允许修改数量 1 允许 2 不允许
//                        DialogUtil.showCustomDialog(viewGroup.getContext(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
//                        return;
//                    }
                    listener.onMaxPlusChild(i, holder1.mRlMax, holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                }
            });

            holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(listChild.get(i).getOad_goodstype()))
                        return;
//                    if ("2".equals(list.get(groupIndex).getFlag())){//是否允许修改数量 1 允许 2 不允许
//                        DialogUtil.showCustomDialog(viewGroup.getContext(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
//                        return;
//                    }
                    listener.onMinMinusChild(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                }
            });

            holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("2".equals(listChild.get(i).getOad_goodstype()))
                        return;
//                    if ("2".equals(list.get(groupIndex).getFlag())){//是否允许修改数量 1 允许 2 不允许
//                        DialogUtil.showCustomDialog(viewGroup.getContext(),"提示","参与活动的订单商品不允许修改商品数量","确定",null,null);
//                        return;
//                    }
                    listener.onMinPlusChild(i, holder1.mRlMin, holder1.mLlMinLeft, holder1.mIvMinPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
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
                listener.onMaxEtNumChild(index, editable.toString(), mEtMaxNum, mEtMinNum, tvSumPrice);
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
                listener.onMinEtNumChild(index, editable.toString(), mEtMaxNum, mEtMinNum, tvSumPrice);
            }
        };

        class ViewHolder {
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
            @BindView(R.id.tv_max_price)
            TextView mTvMaxPrice;
            @BindView(R.id.tv_min_price)
            TextView mTvMinPrice;
            @BindView(R.id.ll_max_price)
            LinearLayout mLlMaxPrice;
            @BindView(R.id.ll_min_price)
            LinearLayout mLlMinPrice;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public interface PHDistributionChildListener {

        void onMaxPriceChild(int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPriceChild(int k, EditText mEtMaxPrice, EditText mEtMinPrice, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxPlusChild(int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinusChild(int k, RelativeLayout mRlMax, LinearLayout mLlMaxLeft, ImageView mIvMax, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlusChild(int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinusChild(int k, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onClearNumChild(int k, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNumChild(int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNumChild(int k, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);
    }


}
