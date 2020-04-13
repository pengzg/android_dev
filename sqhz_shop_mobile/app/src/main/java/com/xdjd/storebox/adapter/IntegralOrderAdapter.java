package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.integralOrderBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/2/24.
 */

public class IntegralOrderAdapter extends BaseAdapter {
    private List<integralOrderBean>list;
    @Override
    public int getCount() {
        return null == list ? 0:list.size();
    }

    public void setData(List<integralOrderBean>list){
        this.list = list;
        notifyDataSetChanged();
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
                    inflate(R.layout.item_integral_order, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.orderTimeTv.setText(list.get(i).getOrderTime());
        holder.orderTypeTv.setText(list.get(i).getOrderStatus());
        holder.goodsTitle.setText(list.get(i).getGoodsTittle());
        holder.integralNum.setText(list.get(i).getGoods_amount()+"积分");
        return view;
    }


    class ViewHolder {
        @BindView(R.id.order_time_tv)
        TextView orderTimeTv;
        @BindView(R.id.order_type_tv)
        TextView orderTypeTv;
        @BindView(R.id.goods_title)
        TextView goodsTitle;
        @BindView(R.id.integral_num)
        TextView integralNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
