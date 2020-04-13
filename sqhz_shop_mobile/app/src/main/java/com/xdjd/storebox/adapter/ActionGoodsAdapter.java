package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.adapter.listener.PurchaseGoodsListener;
import com.xdjd.storebox.bean.ActionBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动商品adapter
 * Created by lijipei on 2016/11/28.
 */

public class ActionGoodsAdapter extends BaseAdapter {

    List<ActionBean> list;
    private PurchaseGoodsListener listener;

    public ActionGoodsAdapter(PurchaseGoodsListener listener) {
        this.listener = listener;
    }

    public void setData(List<ActionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_goods_gv, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).
                load(list.get(i).getBpa_path()).into(holder.mGoodsIv);

        holder.mGoodsName.setText(list.get(i).getGg_title());

        //如果后台没有返回起订量,设置默认为1
        if ("".equals(list.get(i).getGps_min_num()) || list.get(i).getGps_min_num() == null || list.get(i).getGps_min_num().equals("0")) {
            list.get(i).setGps_min_num("1");
        }
        //如果后台没有设置商品的增量,默认设置为1
        if ("".equals(list.get(i).getGps_add_num()) || list.get(i).getGps_add_num() == null) {
            list.get(i).setGps_add_num("1");
        }

        //判断库存字段是否为空
        if (null == list.get(i).getGoods_stock() || "".equals(list.get(i).getGoods_stock())) {
            list.get(i).setGoods_stock("0");
        }

        //如果是临期商品
        if ("14".equals(list.get(i).getGgp_goods_type())) {
        } else {
            holder.mGoodsWholesalePrice.setText("¥" +
                    list.get(i).getGps_price_min()+"/"+list.get(i).getGg_unit_min_nameref());
            if(list.get(i).getGps_price_max() != null && !list.get(i).getGps_price_max().equals("")&&
                    !list.get(i).getGps_price_min().equals(list.get(i).getGps_price_max())){
                holder.mGoodsBatchPrice.setText("¥"+list.get(i).getGps_price_max()+"/"+list.get(i).getGg_unit_max_nameref());
                holder.mGoodsBatchPrice.setVisibility(View.VISIBLE);
            }else{
                holder.mGoodsBatchPrice.setVisibility(View.GONE);
            }
        }

        if ("1".equals(list.get(i).getGps_min_num())) {
            holder.mGoodsStandard.setText("规格:" + list.get(i).getGg_model());
        } else {
            holder.mGoodsStandard.setText("规格:" + list.get(i).getGg_model()+ "  起订数量:" + list.get(i).getGps_min_num());
        }
        if ("2".equals(list.get(i).getGgp_goods_type())) {
            //显示预售提示
            holder.mPresellTv.setVisibility(View.VISIBLE);
        }else{
            holder.mPresellTv.setVisibility(View.GONE);
        }

        holder.mGoodsStock.setText("库存:" + list.get(i).getGoods_stock());
        if(list.get(i).getGoods_stock().equals("0")){
            holder.mStockoutTv.setVisibility(View.VISIBLE);
            holder.mPlus_minus_rl.setVisibility(View.GONE);
        }else{
            holder.mStockoutTv.setVisibility(View.GONE);
            holder.mPlus_minus_rl.setVisibility(View.VISIBLE);
        }
        init(i, holder.mPlus_minus_rl, holder);
        return view;
    }

    private void init(final int i, final RelativeLayout rl, final ViewHolder holder) {
        holder.mPlus_minus_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editGoodCartNumActicon(i,rl,list.get(i));
            }
        });
        holder.mItemGoodsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemGoods(i);
            }
        });
    }


    static class ViewHolder {
        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.presell_tv)
        TextView mPresellTv;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_standard)
        TextView mGoodsStandard;
        @BindView(R.id.goods_stock)
        TextView mGoodsStock;
        @BindView(R.id.item_goods_ll)
        LinearLayout mItemGoodsLl;
        @BindView(R.id.goods_wholesale_price)
        TextView mGoodsWholesalePrice;
        @BindView(R.id.goods_batch_price)
        TextView mGoodsBatchPrice;
        @BindView(R.id.stockout_tv)
        TextView mStockoutTv;
        @BindView(R.id.goods_plus_iv)
        ImageView mGoodsPlusIv;
        @BindView (R.id.plus_minus_rl)
        RelativeLayout mPlus_minus_rl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
