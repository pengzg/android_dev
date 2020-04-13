package com.xdjd.distribution.adapter.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.xdjd.distribution.adapter.RefundAdapter;
import com.xdjd.distribution.bean.GoodsBean;
import com.xdjd.utils.AnimUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2018/2/2
 *     desc   : 商品订单确认列表adapter
 *     version: 1.0
 * </pre>
 */

public class GoodsOrderSubmitAdapter extends RecyclerView.Adapter<GoodsOrderSubmitAdapter.MyViewHolder> {

    public List<GoodsBean> list;
    Context mContext;

    private GoodsOrderSubmitListener listener;
    private int index;
    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private TextView tvSumPrice;

    public void setListener(GoodsOrderSubmitListener listener) {
        this.listener = listener;
    }

    public GoodsOrderSubmitAdapter(Context context, List<GoodsBean> list) {
        this.list = list;
        mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public GoodsOrderSubmitAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_declare_goods_listing, parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(GoodsOrderSubmitAdapter.MyViewHolder holder, final int i) {
        holder.mTvIndex.setText(String.valueOf(i + 1));
        holder.mTvName.setText(list.get(i).getGg_title() + " " + list.get(i).getGg_model());
        holder.mTvStock.setText(list.get(i).getStock_nameref());

        holder.mTvMaxUnitName.setText(list.get(i).getGg_unit_max_nameref());
        holder.mTvMinUnitName.setText(list.get(i).getGg_unit_min_nameref());

        //如果大小单位换算比==1,隐藏大单位
        if (list.get(i).getGgp_unit_num().equals("1")){
            holder.mLlMaxMain.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinUnit.setText(list.get(i).getMin_price() + "元/" + list.get(i).getGg_unit_min_nameref());
        }else{
            holder.mLlMaxMain.setVisibility(View.VISIBLE);
            holder.mTvMaxUnit.setText(list.get(i).getMax_price() + "元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText(list.get(i).getMin_price() + "元/" + list.get(i).getGg_unit_min_nameref());
        }

        holder.mEtMaxNum.setText(list.get(i).getMaxNum());
        holder.mEtMinNum.setText(list.get(i).getMinNum());

        holder.mTvTotalPrice.setText(list.get(i).getTotalPrice());

        AnimUtils.setTranslateAnimation(holder.mEtMaxNum,holder.mLlMaxLeft,holder.mRlMax, holder.mIvMaxPlus);
        AnimUtils.setTranslateAnimation(holder.mEtMinNum,holder.mLlMinLeft,holder.mRlMin,holder.mIvMinPlus);

        final MyViewHolder holder1 = holder;

        holder.mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClearNum(i, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
                AnimUtils.setTranslateAnimation(holder1.mEtMaxNum,holder1.mLlMaxLeft,holder1.mRlMax, holder1.mIvMaxPlus);
                AnimUtils.setTranslateAnimation(holder1.mEtMinNum,holder1.mLlMinLeft,holder1.mRlMin,holder1.mIvMinPlus);
            }
        });

        holder.mIvMaxMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxMinus(i,holder1.mRlMax,holder1.mLlMaxLeft,holder1.mIvMaxPlus ,holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMaxPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMaxPlus(i,holder1.mRlMax,holder1.mLlMaxLeft, holder1.mIvMaxPlus, holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinMinus(i,holder1.mRlMin,holder1.mLlMinLeft,holder1.mIvMinPlus , holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        holder.mIvMinPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMinPlus(i, holder1.mRlMin,holder1.mLlMinLeft,holder1.mIvMinPlus,holder1.mEtMaxNum, holder1.mEtMinNum, holder1.mTvTotalPrice);
            }
        });

        final MyViewHolder holder2 = holder;
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
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
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
        @BindView(R.id.tv_clear)
        TextView mTvClear;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
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

    public interface GoodsOrderSubmitListener {
        void onMaxPlus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft,ImageView mIvMax,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxMinus(int i, RelativeLayout mRlMax, LinearLayout mLlMaxLeft,ImageView mIvMax ,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinPlus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft,ImageView mIvMin,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinMinus(int i, RelativeLayout mRlMin, LinearLayout mLlMinLeft, ImageView mIvMin,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onClearNum(int i,EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMaxEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);

        void onMinEtNum(int i, String num, EditText mEtMaxNum, EditText mEtMinNum, TextView tvSumPrice);
    }

    /*之下的方法都是为了方便操作，并不是必须的*/

    //在指定位置插入，原位置的向后移动一格
   /* public boolean addItem(int position, String msg) {
        if (position < list.size() && position >= 0) {
            list.add(position, msg);
            notifyItemInserted(position);
            return true;
        }
        return false;
    }

    //去除指定位置的子项
    public boolean removeItem(int position) {
        if (position < list.size() && position >= 0) {
            list.remove(position);
            notifyItemRemoved(position);
            return true;
        }
        return false;
    }

    //清空显示数据
    public void clearAll() {
        list.clear();
        notifyDataSetChanged();
    }*/
}
