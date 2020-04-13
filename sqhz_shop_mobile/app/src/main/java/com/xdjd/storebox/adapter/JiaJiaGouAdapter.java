package com.xdjd.storebox.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.JiaJiaGouBean;
import com.xdjd.utils.UIUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2016/12/16.
 */

public class JiaJiaGouAdapter extends BaseAdapter {

    private List<JiaJiaGouBean> list;

    private JiaJiaGouListener listener;
    private BigDecimal amount; //购物车总价格
    private String giftgoodsid;//选中的商品id

    public JiaJiaGouAdapter(JiaJiaGouListener listener,BigDecimal amount) {
        this.listener = listener;
        this.amount = amount;
    }


    public void setData(List<JiaJiaGouBean> list,String giftgoodsid) {
        this.list = list;
        this.giftgoodsid = giftgoodsid;
        notifyDataSetChanged();
    }

    public void setGiftgoodsid(String giftgoodsid){
        this.giftgoodsid = giftgoodsid;
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
                    R.layout.item_jiajiagou_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load(list.get(i).getCover()).into(holder.mGoodsIv);
        holder.mGoodsName.setText(list.get(i).getGb_title());
        holder.mGoodsPrice.setText(UIUtils.getString(R.string.money).toString() + list.get(i).getWpg_goods_amount());
        holder.mGoodsCostPrice.setText("原价:" + list.get(i).getGp_wholesale_price() + "元");
        holder.mGoodsCostPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mGoodsBottomPrice.setText("满" + list.get(i).getWpg_min_amount() + "元可换购(商品仅剩"+list.get(i).getLeftNum()+")");

        if (amount.doubleValue()>list.get(i).getWpg_min_amount() && list.get(i).getLeftNum()>0){
            holder.mSelectGoodsRl.setVisibility(View.VISIBLE);
            if (giftgoodsid!=null && list.get(i).getWpg_id().equals(giftgoodsid)){
                holder.mSelectIv.setSelected(true);
            }else{
                holder.mSelectIv.setSelected(false);
            }
        }else{
            holder.mSelectGoodsRl.setVisibility(View.INVISIBLE);
        }

        holder.mSelectGoodsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.checkGoods(i);
            }
        });

        return view;
    }

    public interface JiaJiaGouListener {
        void checkGoods(int position);
    }

    class ViewHolder {
        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.goods_name)
        TextView mGoodsName;
        @BindView(R.id.goods_price)
        TextView mGoodsPrice;
        @BindView(R.id.goods_cost_price)
        TextView mGoodsCostPrice;
        @BindView(R.id.goods_bottom_price)
        TextView mGoodsBottomPrice;
        @BindView(R.id.select_iv)
        ImageView mSelectIv;
        @BindView(R.id.select_goods_rl)
        RelativeLayout mSelectGoodsRl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
