package com.xdjd.distribution.adapter;

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
import com.xdjd.utils.UIUtils;

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

public class NewGoRolloutGoodsAdapter extends BaseAdapter {

    public List<GoodsBean> list;
    private GoRolloutGoodsListener listener;
    private int index;
    private EditText mEtMaxNum;
    private EditText mEtMinNum;
    private TextView tvSumPrice;

    public List<GoodsBean> getList() {
        return list;
    }

    public NewGoRolloutGoodsAdapter(GoRolloutGoodsListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_new_go_rollout_goods_cart, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvName.setText(list.get(i).getGg_title());

        //如果大小单位换算比==1隐藏大单位
        if ("1".equals(list.get(i).getGgp_unit_num())) {
            holder.mLlMax.setVisibility(View.INVISIBLE);
            holder.mTvMaxUnit.setText("");
            holder.mTvMinPrice.setText(list.get(i).getMin_price());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        } else {
            holder.mLlMax.setVisibility(View.VISIBLE);
            holder.mTvMaxPrice.setText(list.get(i).getMax_price());//大单位价格
            holder.mTvMinPrice.setText(list.get(i).getMin_price());//小单位价格
            holder.mTvMaxUnit.setText("元/" + list.get(i).getGg_unit_max_nameref());
            holder.mTvMinUnit.setText("元/" + list.get(i).getGg_unit_min_nameref());
        }

        if ("Y".equals(list.get(i).getIsHasCart())){
            holder.mRlEditCartNum.setBackground(UIUtils.getDrawable(R.drawable.bg_oval_stroke_blue));
            holder.mIvCart.setImageResource(R.mipmap.cart_blue);
        }else{
            holder.mRlEditCartNum.setBackground(UIUtils.getDrawable(R.drawable.bg_oval_stroke_gray));
            holder.mIvCart.setImageResource(R.mipmap.cart_gray);
        }

        String stockStr = UnitCalculateUtils.unitStr(list.get(i).getGgp_unit_num(), list.get(i).getGgs_stock(),
                list.get(i).getGg_unit_max_nameref(), list.get(i).getGg_unit_min_nameref());
        holder.mTvStock.setText(stockStr);
        holder.mRlEditCartNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEditCartNum(i);
            }
        });

        return view;
    }

    public interface GoRolloutGoodsListener {
        void onEditCartNum(int i);
    }

    static class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_max_unit)
        TextView mTvMaxUnit;
        @BindView(R.id.tv_min_unit)
        TextView mTvMinUnit;
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
        @BindView(R.id.rl_edit_cart_num)
        RelativeLayout mRlEditCartNum;
        @BindView(R.id.iv_cart)
        ImageView mIvCart;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
