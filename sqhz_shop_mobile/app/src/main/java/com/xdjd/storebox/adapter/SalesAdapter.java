package com.xdjd.storebox.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.HomeActivityBean;
import com.xdjd.storebox.bean.HomeBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动列表adapter
 * Created by lijipei on 2016/12/9.
 */

public class SalesAdapter extends BaseAdapter {

    private List<HomeActivityBean> list;
    private boolean isScroll;
    private int mLastIndex;

    public void setData(List<HomeActivityBean> list){
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
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sales, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder= (ViewHolder) view.getTag();
        }

        if (isScroll) {
            ObjectAnimator
                    .ofFloat(view, "translationY",
                            mLastIndex < i ? 80 : -80, 0)
                    .setDuration(300).start();

        }
        mLastIndex = i ;
        Glide.with(viewGroup.getContext()).load(list.get(i).getActivityCover()).
                into(holder.mSalesIvBig);
        return view;
    }

    public boolean isScroll() {
        return isScroll;
    }

    public void setScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    class ViewHolder {
        @BindView(R.id.sales_iv_big)
        ImageView mSalesIvBig;
//        @BindView(R.id.sales_iv_small)
//        ImageView mSalesIvSmall;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
