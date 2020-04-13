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
import com.xdjd.storebox.bean.GoodsBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 采购商品adapter
 * Created by lijipei on 2016/11/21.
 */

public class PurchaseGoodsAdapter extends BaseAdapter {

    List<GoodsBean> list;

    private int columns;
    private PurchaseGoodsListener listener;

    private boolean isSuccess = false;

    public PurchaseGoodsAdapter(int columns, PurchaseGoodsListener listener) {
        this.columns = columns;
        this.listener = listener;
    }

    public void setData(List<GoodsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setColumns(int columns) {
        this.columns = columns;
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
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View viewList = null;
        View viewGrid = null;
        switch (columns) {
            case 1:
                ViewHolderList holderList = null;
                if (viewList == null) {
                    viewList = LayoutInflater.from(viewGroup.getContext()).
                            inflate(R.layout.item_goods_listview, viewGroup, false);
                    holderList = new ViewHolderList(viewList);
                    viewList.setTag(holderList);
                    view = viewList;
                    AutoUtils.auto(view);
                } else {
                    holderList = (ViewHolderList) view.getTag();
                }
                Glide.with(viewGroup.getContext()).
                        load(list.get(i).getBpa_path()).into(holderList.mGoodsIv);

                if ("2".equals(list.get(i).getGgp_goods_type())) {
                    //显示预售提示
                    holderList.mPresellTv.setVisibility(View.VISIBLE);
                }else{
                    holderList.mPresellTv.setVisibility(View.GONE);
                }

                holderList.mGoodsName.setText(list.get(i).getGg_title());

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
                if ("0".equals(list.get(i).getGps_min_num())) {
                    holderList.mGoodsStandard.setText("规格:" + list.get(i).getGg_model());
                } else {
                    holderList.mGoodsStandard.setText("规格:" + list.get(i).getGg_model() +
                            "  起订数量:" + list.get(i).getGps_min_num());
                }
                holderList.mGoodsWholesalePrice.setText("¥" +
                        list.get(i).getGps_price_min()+"/"+list.get(i).getGg_unit_min_nameref());
                if(list.get(i).getGps_price_max() != null && !list.get(i).getGps_price_max().equals("")&&
                        !list.get(i).getGps_price_min().equals(list.get(i).getGps_price_max())){
                    holderList.mGoodsBatchPrice.setText("¥"+list.get(i).getGps_price_max()+"/"+list.get(i).getGg_unit_max_nameref());
                    holderList.mGoodsBatchPrice.setVisibility(View.VISIBLE);
                }else{
                    holderList.mGoodsBatchPrice.setVisibility(View.GONE);
                }
                holderList.mGoodsStock.setText("库存:"+list.get(i).getGoods_stock());
                if(list.get(i).getGoods_stock().equals("0")){
                    holderList.mStockoutTv.setVisibility(View.VISIBLE);
                    holderList.mPlus_minus_rl.setVisibility(View.GONE);
                }else{
                    holderList.mStockoutTv.setVisibility(View.GONE);
                    holderList.mPlus_minus_rl.setVisibility(View.VISIBLE);
                }
                initList(i, holderList.mPlus_minus_rl, holderList);
                break;
            case 2:
                ViewHolderGrid holderGrid = null;
                if (viewGrid == null) {
                    viewGrid = LayoutInflater.from(viewGroup.getContext()).
                            inflate(R.layout.item_goods_gridview, viewGroup, false);
                    holderGrid = new ViewHolderGrid(viewGrid);
                    viewGrid.setTag(holderGrid);
                    view = viewGrid;
                    AutoUtils.auto(view);
                } else {
                    holderGrid = (ViewHolderGrid) view.getTag();
                }

                Glide.with(viewGroup.getContext()).
                        load(list.get(i).getBpa_path()).into(holderGrid.mGoodsIv);

                if ("2".equals(list.get(i).getGgp_goods_type())) {
                    //显示预售提示
                    holderGrid.mPresellTv.setVisibility(View.VISIBLE);
                }else{
                    holderGrid.mPresellTv.setVisibility(View.GONE);
                }

                holderGrid.mGoodsName.setText(list.get(i).getGg_title());
                if ("0".equals(list.get(i).getGps_min_num())) {
                    holderGrid.mGoodsStandard.setText("规格:" + list.get(i).getGg_model());
                } else {
                    holderGrid.mGoodsStandard.setText("规格:" + list.get(i).getGg_model() +
                            "  起订数量:" + list.get(i).getGps_min_num());
                }
                holderGrid.mGoodsWholesalePrice.setText("¥" +
                        list.get(i).getGps_price_min()+"/"+list.get(i).getGg_unit_min_nameref());
                if(list.get(i).getGps_price_max() != null && !list.get(i).getGps_price_max().equals("")&&
                        !list.get(i).getGps_price_min().equals(list.get(i).getGps_price_max())){
                    holderGrid.mGoodsBatchPrice.setText("¥"+list.get(i).getGps_price_max()+"/"+list.get(i).getGg_unit_max_nameref());
                    holderGrid.mGoodsBatchPrice.setVisibility(View.VISIBLE);
                }else{
                    holderGrid.mGoodsBatchPrice.setVisibility(View.GONE);
                }
                holderGrid .mGoodsStock.setText("库存:"+list.get(i).getGoods_stock());
                if(list.get(i).getGoods_stock().equals("0")){
                    holderGrid.mStockoutTv.setVisibility(View.VISIBLE);
                    holderGrid.mPlus_minus_rl.setVisibility(View.GONE);
                }else{
                    holderGrid.mStockoutTv.setVisibility(View.GONE);
                    holderGrid.mPlus_minus_rl.setVisibility(View.VISIBLE);
                }
                initGrid(i, holderGrid.mPlus_minus_rl, holderGrid);
                break;
        }
        return view;
    }


    private void initGrid(final int i, final RelativeLayout rl, final ViewHolderGrid holderGrid) {
        holderGrid.mPlus_minus_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editGoodsCartNumNew(i,rl,list.get(i));
            }
        });

        holderGrid.mItemGoodsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemGoods(i);
            }
        });
    }

    private void initList(final int i, final RelativeLayout rl, final ViewHolderList holderList) {
        holderList.mPlus_minus_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editGoodsCartNumNew(i,rl,list.get(i));
            }
        });
        holderList.mItemGoodsLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemGoods(i);
            }
        });
    }

    class ViewHolderGrid {
        @BindView(R.id.item_goods_ll)
        LinearLayout mItemGoodsLl;
        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.presell_tv)
        TextView mPresellTv;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_standard)
        TextView mGoodsStandard;
        @BindView(R.id.goods_wholesale_price)
        TextView mGoodsWholesalePrice;
        @BindView(R.id.goods_stock)
        TextView mGoodsStock;
        @BindView(R.id.goods_batch_price)
        TextView mGoodsBatchPrice;
        @BindView(R.id.stockout_tv)
        TextView mStockoutTv;
        @BindView (R.id.plus_minus_rl)
        RelativeLayout mPlus_minus_rl;
        ViewHolderGrid(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderList {
        @BindView(R.id.item_goods_ll)
        LinearLayout mItemGoodsLl;
        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.presell_tv)
        TextView mPresellTv;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_standard)
        TextView mGoodsStandard;
        @BindView(R.id.goods_wholesale_price)
        TextView mGoodsWholesalePrice;
        @BindView(R.id.goods_stock)
        TextView mGoodsStock;
        @BindView(R.id.goods_batch_price)
        TextView mGoodsBatchPrice;
        @BindView(R.id.stockout_tv)
        TextView mStockoutTv;
        @BindView (R.id.plus_minus_rl)
        RelativeLayout mPlus_minus_rl;

        ViewHolderList(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
