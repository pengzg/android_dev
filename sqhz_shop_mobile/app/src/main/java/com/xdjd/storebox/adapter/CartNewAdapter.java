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
import com.xdjd.storebox.adapter.listener.CartGoodsListener;
import com.xdjd.storebox.adapter.listener.CartListener;
import com.xdjd.storebox.bean.CartActionBean;
import com.xdjd.storebox.bean.CartGoodsListBean;
import com.xdjd.storebox.bean.GiftListBean;
import com.xdjd.view.NoScrollListView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车adapter
 * Created by lijipei on 2016/11/28.
 */

public class CartNewAdapter extends BaseAdapter {

    /**
     * 是否显示编辑
     */
    public static boolean isSuccess;
    /**
     * 是否全部选中商品
     */
    public static boolean isAll;

    public List<CartActionBean> list;

    private CartListener listener;

    public CartNewAdapter(CartListener listener) {
        this.listener = listener;
    }

    public void setData(List<CartActionBean> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_cart_list_action, viewGroup, false);
            holder = new ViewHolder(view);
            holder.adapterGoods = new CartGoodsAdapter();
            holder.mGoodsListview.setAdapter(holder.adapterGoods);
            holder.adapterGift = new CartGiftAdapter();
            holder.mActionGoodsListview.setAdapter(holder.adapterGift);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (list.get(i).getActivityId() == null || "0".equals(list.get(i).getActivityId())) {
            holder.mLlCartAction.setVisibility(View.GONE);
        } else {
            holder.mLlCartAction.setVisibility(View.VISIBLE);
            holder.mTvCartAction.setText(list.get(i).getActivityName());

            holder.mLlCartAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.cartAction(i);
                }
            });
        }

        //判断是否有
        if (list.get(i).getGiftList() == null || list.get(i).getGiftList().size() == 0) {
            holder.mActionGoodsLvLl.setVisibility(View.GONE);
        } else {
            holder.mActionGoodsLvLl.setVisibility(View.VISIBLE);
            holder.adapterGift.setGiftData(list.get(i).getGiftList());
        }

        if ("5".equals(list.get(i).getActivityType())){
            holder.mLlCombo.setVisibility(View.VISIBLE);
            if (list.get(i).getIsChild() == 0) {
                holder.mIvGround.setSelected(false);
            } else if (list.get(i).getIsChild() == 1) {
                holder.mIvGround.setSelected(true);
            }
        }else{
            holder.mLlCombo.setVisibility(View.GONE);
        }

        holder.adapterGoods.setGoodsData(i, list.get(i).getDataList());

        holder.mGoodsBuyNum.setText(list.get(i).getActivityNum());

        holder.mGoodsPlusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartPlus(i);
            }
        });

        holder.mGoodsMinusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartMinus(i);
            }
        });

        holder.mGoodsBuyNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartEdit(i);
            }
        });

        holder.mRlGroundAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartLeft(i);
            }
        });

        holder.adapterGoods.goodsListener(new CartGoodsListener() {
            @Override
            public void deleteItem(int k) {
                listener.cartChildDelete(i, k);
            }

            @Override
            public void goodsPlusCart(int k) {
                listener.cartPlus(i, k);
            }

            @Override
            public void goodsMinusCart(int k) {
                listener.cartMinus(i, k);
            }

            @Override
            public void goodsEditCart(int k) {
                listener.cartEdit(i, k);
            }

            @Override
            public void goodsLeft(int k) {
                listener.cartLeft(i, k);
            }

            @Override
            public void goodsItem(int k) {
                listener.cartChildItem(i, k);
            }

        });


        return view;
    }

    class ViewHolder {
        @BindView(R.id.action_goods_listview)
        NoScrollListView mActionGoodsListview;
        @BindView(R.id.goods_listview)
        NoScrollListView mGoodsListview;
        @BindView(R.id.action_goods_lv_ll)
        LinearLayout mActionGoodsLvLl;
        @BindView(R.id.tv_cart_action)
        TextView mTvCartAction;
        @BindView(R.id.ll_cart_action)
        LinearLayout mLlCartAction;
        @BindView(R.id.iv_ground)
        ImageView mIvGround;
        @BindView(R.id.rl_ground_all)
        RelativeLayout mRlGroundAll;
        @BindView(R.id.goods_minus_iv)
        ImageView mGoodsMinusIv;
        @BindView(R.id.goods_buy_num)
        TextView mGoodsBuyNum;
        @BindView(R.id.goods_plus_iv)
        ImageView mGoodsPlusIv;
        @BindView(R.id.rl_edit_num)
        RelativeLayout mRlEditNum;
        @BindView(R.id.ll_combo)
        LinearLayout mLlCombo;

        CartGoodsAdapter adapterGoods;

        CartGiftAdapter adapterGift;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 商品列表adapter
     */
    public class CartGoodsAdapter extends BaseAdapter {

        List<CartGoodsListBean> listGoods;

        private CartGoodsListener listener;
        private int groupIndex;

        public void goodsListener(CartGoodsListener listener) {
            this.listener = listener;
        }

        public void setGoodsData(int groupIndex, List<CartGoodsListBean> listGoods) {
            this.listGoods = listGoods;
            this.groupIndex = groupIndex;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listGoods == null ? 0 : listGoods.size();
        }

        @Override
        public Object getItem(int i) {
            return listGoods.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolderGoods holderGoods;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_cart_list_goods, viewGroup, false);
                holderGoods = new ViewHolderGoods(view);
                view.setTag(holderGoods);
                AutoUtils.auto(view);
            } else {
                holderGoods = (ViewHolderGoods) view.getTag();
            }

            if ("5".equals(list.get(groupIndex).getActivityType())) {
                //组合促销--不显示商品加减,和是够勾选按钮
                //                holderGoods.mRlEditNum.setVisibility(View.INVISIBLE);
                holderGoods.mDelete.setVisibility(View.GONE);
                holderGoods.mItemGoodsLl.setVisibility(View.VISIBLE);

                holderGoods.mGoodsPlusIv.setVisibility(View.INVISIBLE);
                holderGoods.mGoodsMinusIv.setVisibility(View.INVISIBLE);
                holderGoods.mGoodsBuyNum.setEnabled(false);

                holderGoods.mChildRl.setVisibility(View.INVISIBLE);

                holderGoods.mGoodsBuyNum.setText(listGoods.get(i).getC_goods_num() +
                        listGoods.get(i).getGg_unit_min_nameref());
            } else {//不是组合促销
                holderGoods.mGoodsPlusIv.setVisibility(View.VISIBLE);
                holderGoods.mGoodsMinusIv.setVisibility(View.VISIBLE);
                holderGoods.mGoodsBuyNum.setEnabled(true);

                holderGoods.mChildRl.setVisibility(View.VISIBLE);
                if (listGoods.get(i).getIsChild() == 0) {
                    holderGoods.mChildIv.setSelected(false);
                } else if (listGoods.get(i).getIsChild() == 1) {
                    holderGoods.mChildIv.setSelected(true);
                }

                if (CartNewAdapter.isSuccess) {
                    holderGoods.mItemGoodsLl.setVisibility(View.GONE);
                    holderGoods.mDelete.setVisibility(View.VISIBLE);
                } else {
                    holderGoods.mDelete.setVisibility(View.GONE);
                    holderGoods.mItemGoodsLl.setVisibility(View.VISIBLE);
                }

                holderGoods.mGoodsBuyNum.setText(listGoods.get(i).getC_goods_num());
            }


            holderGoods.mChildRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goodsLeft(i);
                }
            });

            Glide.with(viewGroup.getContext()).load(listGoods.get(i).getBpa_path()).
                    into(holderGoods.mGoodsIv);

            if ("2".equals(listGoods.get(i).getGgp_goods_type())) {
                //显示预售提示
                holderGoods.mPresellTv.setVisibility(View.VISIBLE);
            } else {
                holderGoods.mPresellTv.setVisibility(View.GONE);
            }

            holderGoods.mGoodsName.setText(listGoods.get(i).getGg_title() + listGoods.get(i).getGg_model());
            //价格
            holderGoods.mGoodsWholesalePrice.setText("¥" + listGoods.get(i).getGps_price_min());

            holderGoods.mGoodsTotalAmount.setText("小计:¥" + listGoods.get(i).getC_goods_amount());

            //起订量
            if ("1".equals(listGoods.get(i).getGps_min_num()) || "0".equals(listGoods.get(i).getGps_min_num())) {
                holderGoods.mGoodsStartNum.setVisibility(View.GONE);
            } else {
                holderGoods.mGoodsStartNum.setVisibility(View.VISIBLE);
                holderGoods.mGoodsStartNum.setText("起订量:" + listGoods.get(i).getGps_min_num());
            }

            //            if (Integer.parseInt(listGoods.get(i).getGp_stock()) >
            //                    Integer.parseInt(listGoods.get(i).getGp_alarm_stock())) {
            //                //库存量大于警戒库存量,就不显示库存
            //                holderGoods.mGoodsStock.setVisibility(View.INVISIBLE);
            //            } else {
            //                holderGoods.mGoodsStock.setVisibility(View.VISIBLE);
            holderGoods.mGoodsStock.setText("库存:" + listGoods.get(i).getGoods_stock());
            //            }

            holderGoods.mItemGoodsLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goodsItem(i);
                }
            });

            holderGoods.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.deleteItem(i);
                }
            });

            holderGoods.mGoodsPlusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goodsPlusCart(i);
                }
            });

            holderGoods.mGoodsMinusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goodsMinusCart(i);
                }
            });

            holderGoods.mGoodsBuyNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.goodsEditCart(i);
                }
            });

            return view;
        }

        class ViewHolderGoods {
            @BindView(R.id.child_iv)
            ImageView mChildIv;
            @BindView(R.id.presell_tv)
            TextView mPresellTv;
            @BindView(R.id.child_rl)
            RelativeLayout mChildRl;
            @BindView(R.id.goods_iv)
            ImageView mGoodsIv;
            @BindView(R.id.goods_name)
            TextView mGoodsName;
            @BindView(R.id.goods_start_num)
            TextView mGoodsStartNum;
            @BindView(R.id.goods_stock)
            TextView mGoodsStock;
            @BindView(R.id.item_goods_ll)
            LinearLayout mItemGoodsLl;
            @BindView(R.id.delete)
            TextView mDelete;
            @BindView(R.id.goods_minus_iv)
            ImageView mGoodsMinusIv;
            @BindView(R.id.goods_buy_num)
            TextView mGoodsBuyNum;
            @BindView(R.id.goods_plus_iv)
            ImageView mGoodsPlusIv;
            @BindView(R.id.goods_wholesale_price)
            TextView mGoodsWholesalePrice;
            @BindView(R.id.goods_total_amount)
            TextView mGoodsTotalAmount;
            @BindView(R.id.rl_edit_num)
            RelativeLayout mRlEditNum;
            //            @BindView(R.id.goods_batch_price)
            //            TextView mGoodsBatchPrice;

            ViewHolderGoods(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    /**
     * 购物车赠品列表
     */
    public class CartGiftAdapter extends BaseAdapter {

        private List<GiftListBean> listGift;

        public void setGiftData(List<GiftListBean> listGift) {
            this.listGift = listGift;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listGift == null ? 0 : listGift.size();
        }

        @Override
        public Object getItem(int i) {
            return listGift.get(i);
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
                        inflate(R.layout.item_cart_list_gift, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
                AutoUtils.auto(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Glide.with(viewGroup.getContext()).load(listGift.get(i).getGoodsImage()).into(holder.mGiftImg);

            holder.mGiftName.setText(listGift.get(i).getGoodsName());
            holder.mGiftDesc.setText(listGift.get(i).getActivityDesc());

            return view;
        }

        class ViewHolder {
            @BindView(R.id.gift_img)
            ImageView mGiftImg;
            @BindView(R.id.gift_name)
            TextView mGiftName;
            @BindView(R.id.gift_desc)
            TextView mGiftDesc;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
