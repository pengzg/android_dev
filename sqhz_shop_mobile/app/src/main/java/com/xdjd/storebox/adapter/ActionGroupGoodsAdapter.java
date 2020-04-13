package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.ActionBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/13.
 */

public class ActionGroupGoodsAdapter extends BaseAdapter {
    private List<ActionBean> list;
    public void setData(List<ActionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_action_group, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(viewGroup.getContext()).load(list.get(i).getBpa_path()).into(holder.ivGoods);
        holder.goodName.setText(list.get(i).getGg_title());
        if(list.get(i).getPag_goods_num()!= null ){
            holder.actionNum.setText("x"+list.get(i).getPag_goods_num());
        }else{
            holder.actionNum.setText("");
        }
        holder.goodPrice.setText("¥"+list.get(i).getGps_price_min());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.iv_goods)
        ImageView ivGoods;
        @BindView(R.id.good_name)
        TextView goodName;
        @BindView(R.id.good_price)
        TextView goodPrice;
        @BindView(R.id.action_num)
        TextView actionNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
