package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.IntegralDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/28.
 */

public class IntegralDetailAdapter extends BaseAdapter {
    private List<IntegralDetailBean> list;
    private int pageNo;

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }

    public void setData(List<IntegralDetailBean> list) {
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
        ViewHolder viewHolder;
        if (null == view) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_integral, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvTitle.setText(list.get(i).getWmad_business_name());//描述
        viewHolder.tvCount.setText(list.get(i).getWmad_change_account());//积分数
        viewHolder.tvTime.setText(list.get(i).getWmad_add_time());//时间
        /*if(i == list.size()){
            viewHolder.bttomLine.setVisibility(View.VISIBLE);
        }else{}*/
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.iv_point)
        ImageView ivPoint;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.bttom_line)
        View bttomLine;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
