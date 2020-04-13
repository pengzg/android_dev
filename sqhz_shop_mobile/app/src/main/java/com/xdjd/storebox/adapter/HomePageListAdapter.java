package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.HomeBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2016/12/22.
 */

public class HomePageListAdapter extends BaseAdapter {

    List<HomeBean.HomePageListBean> list;

    public void setData(List<HomeBean.HomePageListBean> list){
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_homepage_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load(list.get(i).getImage()).placeholder(R.color.image_gray).
                into(holder.mHomepageListImg);
        holder.mHomepageListName.setText(list.get(i).getTitle());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.homepage_list_img)
        ImageView mHomepageListImg;
        @BindView(R.id.homepage_list_name)
        TextView mHomepageListName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
