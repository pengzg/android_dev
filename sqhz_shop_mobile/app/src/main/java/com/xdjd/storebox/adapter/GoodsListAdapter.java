package com.xdjd.storebox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品详情列表bean
 * Created by lijipei on 2016/12/10.
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.GoodsListHolder> {

    private List<OrderGoodsDetailBean> list;
    private Context context;

    public GoodsListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<OrderGoodsDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public GoodsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_goods_description, parent, false);
        GoodsListHolder holder = new GoodsListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GoodsListHolder holder, int position) {
        Glide.with(context).load(list.get(position).
                getBpa_path()).into(holder.mGoodsIv);
        holder.mGoodsName.setText(list.get(position).getGg_title());

        holder.mGoodsNum.setText("x"+list.get(position).getGoods_num());
        holder.mGoodsPrice.setText("¥"+list.get(position).getGps_price_min());

        holder.mGoodsTotalPrice.setText("小计: ¥"+list.get(position).getGoods_amount());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class GoodsListHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_price)
        TextView mGoodsPrice;
        @BindView(R.id.goods_total_price)
        TextView mGoodsTotalPrice;
        @BindView(R.id.goods_num)
        TextView mGoodsNum;
        @BindView(R.id.order_detail_item_linerlayout)
        LinearLayout mOrderDetailItemLinerlayout;

        public GoodsListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
