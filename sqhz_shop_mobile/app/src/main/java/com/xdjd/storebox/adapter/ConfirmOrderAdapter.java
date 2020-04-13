package com.xdjd.storebox.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2016/12/5.
 */

public class ConfirmOrderAdapter extends BaseAdapter {

    private GpidListener listener;
    public ConfirmOrderAdapter(GpidListener listener){
        this.listener = listener;
    }
    List<OrderGoodsDetailBean> list ;

    public void setData(List<OrderGoodsDetailBean> list) {
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
                    R.layout.item_confirm_order, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(i == 0){

        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.getGpid(list.get(i).getOd_goods_price_id(),list.get(i).getOd_price_strategyid());
            }
        });
        if(i == 0){
            holder.firstLine.setVisibility(View.GONE);
        }
        holder.goodsName.setText(list.get(i).getOd_goods_name());//商品名称
        holder.goodsPrice.setText("¥"+list.get(i).getOd_price_min());//单价
        if(list.get(i).getOd_goods_unitname_min().equals(list.get(i).getOd_goods_unitname_max())){
            holder.goodModel.setVisibility(View.GONE);
        }else{
            holder.goodModel.setVisibility(View.VISIBLE);
            holder.goodModel.setText(list.get(i).getOd_unit_num()+list.get(i).getOd_goods_unitname_min()+"/"+
                    list.get(i).getOd_goods_unitname_max());//单位换算
        }
        holder.getNum.setText(list.get(i).getGet_goods_num() + "/");//要货数量
        holder.goodsMoney.setText("¥" + list.get(i).getOd_total_amount());//要货金额
        holder.goodsNum.setText(list.get(i).getDelivery_goods_num() + "/");//发货数量
        holder.goodsAmount.setText("¥" + list.get(i).getOd_delivery_amount());//发货金额
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
        @BindView(R.id.item_id)
        LinearLayout itemId;
        @BindView(R.id.first_line)
        View firstLine;
        @BindView(R.id.good_model)
        TextView goodModel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface GpidListener{
        void getGpid(String gpid,String gppid);
    }
}
