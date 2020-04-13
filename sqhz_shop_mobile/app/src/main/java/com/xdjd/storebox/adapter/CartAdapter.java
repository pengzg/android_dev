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
import com.xdjd.storebox.adapter.listener.CartListener;
import com.xdjd.storebox.bean.CartListBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.NoScrollListView;
import com.zhy.autolayout.utils.AutoUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车adapter
 * Created by lijipei on 2016/11/28.
 */

public class CartAdapter extends BaseAdapter {

    /**
     * 是否显示编辑
     */
    public static boolean isSuccess;
    /**
     * 是否全部选中商品
     */
    public static boolean isAll;

    private List<CartListBean> list;

    private CartListener listener;

    public CartAdapter(CartListener listener) {
        this.listener = listener;
    }

    public void setData(List<CartListBean> list) {
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
        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart_list, viewGroup, false);
            holder = new ViewHolder(view);
//            holder.adapter = new CartChildAdapter();
//            holder.mChildListview.setAdapter(holder.adapter);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load(list.get(i).getDelivery()).
                into(holder.mDeliveryModeIv);
        holder.mDeliverySite.setText(list.get(i).getShopName());
        holder.mServicePhone.setText(list.get(i).getShopTel());
        holder.mStartPrice.setText("¥" + list.get(i).getFreeAmount());

        if (list.get(i).getIsAll() == 0) {
            holder.mAllIv.setSelected(false);
        } else if (list.get(i).getIsAll() == 1) {
            holder.mAllIv.setSelected(true);
        }

        if (isSuccess) {
            holder.mEnterTv.setText("删除");
            holder.mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));

            holder.mCollectTv.setVisibility(View.VISIBLE);
        } else {
            holder.mCollectTv.setVisibility(View.GONE);
            BigDecimal amount = new BigDecimal(list.get(i).getAmount());
            BigDecimal freeAmount = new BigDecimal(list.get(i).getFreeAmount());
            if (freeAmount.doubleValue() > amount.doubleValue()) {
                holder.mEnterTv.setText("¥" + list.get(i).getFreeAmount() + "起送");
                holder.mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_c2c2c2));
            } else {
                holder.mEnterTv.setText("结算(" + list.get(i).getGoodsNum() + ")");
                holder.mEnterTv.setBackgroundColor(UIUtils.getColor(R.color.color_EC193A));
            }
        }

        holder.mSumPrice.setText("共¥" + list.get(i).getAmount());

       // holder.adapter.setChildData(list.get(i).getDataList());

//        holder.adapter.childListener(new CartChildListener() {
//            @Override
//            public void deleteItem(int j) {
//                listener.cartChildDelete(i, j);
//            }
//
//            @Override
//            public void childPlusCart(int j) {
//                listener.cartPlus(i, j);
//            }
//
//            @Override
//            public void childMinusCart(int j) {
//                listener.cartMinus(i, j);
//            }
//
//            @Override
//            public void childEditCart(int j) {
//                listener.cartEdit(i, j);
//            }
//
//            @Override
//            public void childLeft(int j) {
//                listener.cartLeft(i, j);
//            }
//
//            @Override
//            public void childItem(int j) {
//                listener.cartChildItem(i,j);
//            }
//        });

       /* holder.mAllRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartAll(i);
            }
        });

        holder.mServicePhoneLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartPhone(i);
            }
        });

        holder.mSpellLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartSpell(i);
            }
        });

        holder.mCollectTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartCollect(i);
            }
        });

        holder.mEnterTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.cartEnter(i);
            }
        });*/

        return view;
    }

    /**
     * 购物车商品子adapter
     */
   /* public class CartChildAdapter extends BaseAdapter {

        List<CartListBean.CartGoodsListBean> listChild;

        private CartChildListener listener;

        public void childListener(CartChildListener listener) {
            this.listener = listener;
        }

        public void setChildData(List<CartListBean.CartGoodsListBean> listChild) {
            this.listChild = listChild;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return listChild == null ? 0 : listChild.size();
        }

        @Override
        public Object getItem(int i) {
            return listChild.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolderChild holderChild;
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.item_cart_child_list, viewGroup, false);
                holderChild = new ViewHolderChild(view);
                view.setTag(holderChild);
                AutoUtils.auto(view);
            } else {
                holderChild = (ViewHolderChild) view.getTag();
            }

            //list.get(i).setIsChild(list.get(i).getIsChild() == 0?PublicFinal.FIRST:PublicFinal.TWO);
            if (listChild.get(i).getIsChild() == 0) {
                holderChild.mChildIv.setSelected(false);
            } else if (listChild.get(i).getIsChild() == 1) {
                holderChild.mChildIv.setSelected(true);
            }

//            holderChild.mChildRl.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.childLeft(i);
//                }
//            });

            if (CartAdapter.isSuccess) {
                holderChild.mItemGoodsLl.setVisibility(View.GONE);
                holderChild.mDelete.setVisibility(View.VISIBLE);
            } else {
                holderChild.mDelete.setVisibility(View.GONE);
                holderChild.mItemGoodsLl.setVisibility(View.VISIBLE);
            }

            Glide.with(viewGroup.getContext()).load(listChild.get(i).getGoodsImage()).
                    into(holderChild.mGoodsIv);

            if ("15".equals(listChild.get(i).getGp_goods_type())) {
                //显示预售提示
                holderChild.mPresellTv.setVisibility(View.VISIBLE);
            }else{
                holderChild.mPresellTv.setVisibility(View.GONE);
            }

            holderChild.mGoodsName.setText(listChild.get(i).getGoodsName());

            holderChild.mGoodsWholesalePrice.setText("¥" + listChild.get(i).getGoodsPrice());
            //            if (Double.parseDouble(listChild.get(i).getGp_carton_num()) < 1) {
            //                holderChild.mGoodsBatchPrice.setVisibility(View.GONE);
            //            }else{
            //                holderChild.mGoodsBatchPrice.setVisibility(View.VISIBLE);
            //                holderChild.mGoodsBatchPrice.setText("箱规价:" +
            //                        listChild.get(i).getGp_carton_num());
            //            }

            if (listChild.get(i).getGp_minnum().equals("1")) {
                holderChild.mGoodsStartNum.setVisibility(View.INVISIBLE);
            } else {
                holderChild.mGoodsStartNum.setVisibility(View.VISIBLE);
                holderChild.mGoodsStartNum.setText("起订量:" + listChild.get(i).getGp_minnum());
            }

            if (Integer.parseInt(listChild.get(i).getGp_stock()) >
                    Integer.parseInt(listChild.get(i).getGp_alarm_stock())) {
                //库存量大于警戒库存量,就不显示库存
                holderChild.mGoodsStock.setVisibility(View.INVISIBLE);
            } else {
                holderChild.mGoodsStock.setVisibility(View.VISIBLE);
                holderChild.mGoodsStock.setText("库存:" + listChild.get(i).getGp_stock());
            }
            //holder.mGoodsWholesalePrice.setText(""+holder.);

            holderChild.mGoodsBuyNum.setText(listChild.get(i).getGoodsNum());

//            holderChild.mItemGoodsLl.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.childItem(i);
//                }
//            });
//
//            holderChild.mDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.deleteItem(i);
//                }
//            });
//
//            holderChild.mGoodsPlusIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.childPlusCart(i);
//                }
//            });
//
//            holderChild.mGoodsMinusIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.childMinusCart(i);
//                }
//            });
//
//            holderChild.mGoodsBuyNum.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.childEditCart(i);
//                }
//            });

            return view;
        }

        class ViewHolderChild {
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
            //            @BindView(R.id.goods_batch_price)
            //            TextView mGoodsBatchPrice;

            ViewHolderChild(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }*/

    class ViewHolder {
        @BindView(R.id.delivery_mode_iv)
        ImageView mDeliveryModeIv;
        @BindView(R.id.delivery_site)
        TextView mDeliverySite;
        @BindView(R.id.service_phone)
        TextView mServicePhone;
        @BindView(R.id.service_phone_ll)
        LinearLayout mServicePhoneLl;
        @BindView(R.id.start_price)
        TextView mStartPrice;
        @BindView(R.id.spell_ll)
        LinearLayout mSpellLl;
        @BindView(R.id.hint_rl)
        RelativeLayout mHintRl;
        @BindView(R.id.child_listview)
        NoScrollListView mChildListview;
        @BindView(R.id.all_iv)
        ImageView mAllIv;
        @BindView(R.id.all_rl)
        RelativeLayout mAllRl;
        @BindView(R.id.sum_price)
        TextView mSumPrice;
        @BindView(R.id.enter_tv)
        TextView mEnterTv;
        @BindView(R.id.collect_tv)
        TextView mCollectTv;

//        CartChildAdapter adapter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
