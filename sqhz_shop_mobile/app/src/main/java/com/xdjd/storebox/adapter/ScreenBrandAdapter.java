package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.ScreenBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 品牌筛选adapter
 * Created by lijipei on 2016/11/24.
 */

public class ScreenBrandAdapter extends BaseAdapter {

    List<ScreenBean> list;

    public void setData(List<ScreenBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_pw_screen_gridlist, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.autoSize(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (list.get(i).getType() == 0){
            holder.mItemScreenTv.setBackgroundResource(R.color.white);
            holder.mItemScreenTv.setSelected(false);
        }else{
            holder.mItemScreenTv.setBackgroundResource(R.drawable.shape_screen_item);
            holder.mItemScreenTv.setSelected(true);
        }

        holder.mItemScreenTv.setText(list.get(i).getBrandName());

        holder.mItemScreenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.get(i).getType() == 0){
                    list.get(i).setType(1);
                    holder.mItemScreenTv.setBackgroundResource(R.drawable.shape_screen_item);
                    holder.mItemScreenTv.setSelected(true);
                }else{
                    list.get(i).setType(0);
                    holder.mItemScreenTv.setBackgroundResource(R.color.white);
                    holder.mItemScreenTv.setSelected(false);
                }
            }
        });

        return view;
    }

    class ViewHolder {
        @BindView(R.id.item_screen_tv)
        TextView mItemScreenTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
