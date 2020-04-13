package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/12/10.
 */

public class OrderDetailAdapter extends BaseAdapter {
    List<OrderGoodsDetailBean> list;

    public void setData(List<OrderGoodsDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size() > 3 ? 3 : list.size();
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
        ConfirmOrderAdapter.ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_confirm_order, viewGroup, false);
            holder = new ConfirmOrderAdapter.ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ConfirmOrderAdapter.ViewHolder) view.getTag();
        }
        if(i == 0){
            holder.firstLine.setVisibility(View.GONE);
        }
        holder.goodsName .setText(list.get(i).getOd_goods_name());
        //holder.goodsPrice.setText(list.get(i).getGoodsPrice());
        holder.getNum.setText(list.get(i).getGet_goods_num()+"/");//要货数量
        holder.goodsMoney.setText("¥"+list.get(i).getOd_total_amount());//要货金额
        holder.goodsNum.setText(list.get(i).getDelivery_goods_num()+"/");//发货数量
        holder.goodsAmount.setText("¥"+list.get(i).getOd_delivery_amount());//发货金额
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_num)
        TextView goodsNum;
        @BindView(R.id.goods_money)
        TextView goodsMoney;
        @BindView(R.id.get_num)
        TextView getNum;
        @BindView(R.id.goods_amount)
        TextView goodsAmount;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.first_line)
        View firstLine;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
