package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.NetSingleBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by lijipei on 2016/12/13.
 */

public class NetSingleAdapter extends BaseAdapter{

    List<NetSingleBean> list;

    public void setData(List<NetSingleBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_net_single,viewGroup,false);
            holder = new ViewHolder();
            view.setTag(holder);
            AutoUtils.auto(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }


        return view;
    }

    class ViewHolder{

    }
}
