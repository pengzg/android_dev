package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 网单商品列表详情adapter
 * Created by lijipei on 2016/12/5.
 */

public class NetSingleGoodsListAdapter extends BaseAdapter {

    List<OrderGoodsDetailBean> list;

    public void setData(List<OrderGoodsDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 5;//list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;//list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_net_single_goods, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        holder.goodsName .setText(list.get(i).getGoodsName());
//        holder.goodsPrice.setText(list.get(i).getGoodsPrice());
//        holder.getNum.setText(list.get(i).getGoodsNum()+"/");//要货数量
//        holder.goodsMoney.setText("¥"+list.get(i).getTotalAmount());//要货金额
//        holder.goodsNum.setText(list.get(i).getDeliveryNum()+"/");//发货数量
//        holder.goodsAmount.setText("¥"+list.get(i).getDeliveryAmount());//发货金额
        /*holder.mGoodsNum.setText(list.get(i).getGoodsNum());
        holder.mGoodsAmount.setText("¥" + list.get(i).getGoodsAmount());
        holder.mGoodsPrice.setText("¥" + list.get(i).getGoodsPrice());*/
        //holder.mGoodsDesc.setText("规格:"+list.get(i).getStandarDesc());

        return view;
    }


    static class ViewHolder {
//        @BindView(R.id.goods_name)
//        TextView goodsName;
//        @BindView(R.id.goods_num)
//        TextView goodsNum;
//        @BindView(R.id.goods_money)
//        TextView goodsMoney;
//        @BindView(R.id.get_num)
//        TextView getNum;
//        @BindView(R.id.goods_amount)
//        TextView goodsAmount;
//        @BindView(R.id.goods_price)
//        TextView goodsPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
