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

public class ValidGoodsAdapter extends BaseAdapter {

    private List<ActionBean> list;
    private PurchaseGoodsListener listener;

    public ValidGoodsAdapter(PurchaseGoodsListener listener) {
        this.listener = listener;
    }

    public void setData(List<ActionBean> list) {
        this.list = list;
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
                    inflate(R.layout.item_action_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).
                load(list.get(i).getGoods_img_url()).into(holder.mGoodsIv);

        holder.mGoodsName.setText(list.get(i).getGg_title());

        //如果后台没有返回起订量,设置默认为1
        if ("".equals(list.get(i).getGps_min_num()) || list.get(i).getGps_min_num() == null) {
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

        //判断预警库存数量是否为空
        /*if (null == list.get(i).getGp_alarm_stock() || "".equals(list.get(i).getGp_alarm_stock())) {
            list.get(i).setGp_alarm_stock("0");
        }*/

        if ("1".equals(list.get(i).getGps_min_num())) {
            //                    holderGrid.mGoodsStartNum.setVisibility(View.INVISIBLE);
            holder.mGoodsStandard.setText("规格:" + list.get(i).getGg_model());
        } else {
            //                    holderGrid.mGoodsStartNum.setVisibility(View.VISIBLE);
            //                    holderGrid.mGoodsStartNum.setText("  起订数量:" + list.get(i).getGp_minnum());
            holder.mGoodsStandard.setText("规格:" + list.get(i).getGg_model() +
                    "  起订数量:" + list.get(i).getGps_min_num());
        }
        //holderGrid.mGoodsDeliveryMode.setText("配送方式:" + list.get(i).getDelivery());

        /*if (Integer.parseInt(list.get(i).getGoodsStock()) >
                Integer.parseInt(list.get(i).getGp_alarm_stock())) {
            //库存量大于警戒库存量,就不显示库存
            holder.mGoodsStock.setVisibility(View.GONE);
        } else {
            holder.mGoodsStock.setVisibility(View.VISIBLE);
            holder.mGoodsStock.setText("库存:" + list.get(i).getGoodsStock());
        }*/


        holder.mGoodsWholesalePrice.setText("批发价:" +
                list.get(i).getGps_price_min());
        if (Double.parseDouble(list.get(i).getGps_price_max()) < 1) {
            holder.mGoodsBatchPrice.setVisibility(View.GONE);
        } else {
            holder.mGoodsBatchPrice.setVisibility(View.VISIBLE);
            holder.mGoodsBatchPrice.setText("箱规价:" +
                    list.get(i).getGps_price_max());
        }

        //3 加价购0 普通
        /*if ("3".equals(list.get(i).getActivityType())) {
            holder.mGoodsHandleRl.setVisibility(View.GONE);
            holder.mGoodsActionDesc.setVisibility(View.VISIBLE);

            holder.mGoodsActionDesc.setText(list.get(i).getGoodsDesc());
        } else if ("0".equals(list.get(i).getActivityType())) {
            holder.mGoodsHandleRl.setVisibility(View.VISIBLE);
            holder.mGoodsActionDesc.setVisibility(View.GONE);

            //当库存不足时显示"补货中"
            if (list.get(i).getGoodsStock().equals("0")) {
                holder.mStockoutTv.setVisibility(View.VISIBLE);
                holder.mPlusMinusRl.setVisibility(View.GONE);
            } else {
                holder.mStockoutTv.setVisibility(View.GONE);
                holder.mPlusMinusRl.setVisibility(View.VISIBLE);
                holder.mGoodsBuyNum.setText(list.get(i).getCartnum());

                if (list.get(i).getCartnum().equals("0")) {
                    holder.mGoodsMinusLl.setVisibility(View.GONE);
                } else {
                    holder.mGoodsMinusLl.setVisibility(View.VISIBLE);
                }
            }
        }*/


        init(i, holder.mPlusMinusRl, holder);
        return view;
    }

    private void init(final int i, final RelativeLayout rl, final ViewHolder holder) {
        holder.mGoodsPlusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.plusCart(i, rl);
            }
        });

        holder.mGoodsMinusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.minusCart(i, rl);
            }
        });

        holder.mGoodsBuyNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.editGoodsCartNum(i, rl,list.get(i).getGgp_id());
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
        @BindView(R.id.goods_minus_iv)
        ImageView mGoodsMinusIv;
        @BindView(R.id.goods_buy_num)
        TextView mGoodsBuyNum;
        @BindView(R.id.goods_plus_iv)
        ImageView mGoodsPlusIv;
        @BindView(R.id.goods_minus_ll)
        LinearLayout mGoodsMinusLl;
        @BindView(R.id.plus_minus_rl)
        RelativeLayout mPlusMinusRl;
        @BindView(R.id.stockout_tv)
        TextView mStockoutTv;
        /*@BindView(R.id.goods_action_desc)
        TextView mGoodsActionDesc;
        @BindView(R.id.goods_handle_rl)
        RelativeLayout mGoodsHandleRl;*/

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
