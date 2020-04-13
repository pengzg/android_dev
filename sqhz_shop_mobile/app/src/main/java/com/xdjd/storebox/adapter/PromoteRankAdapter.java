package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.PromoteRankBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/1/10.
 */

public class PromoteRankAdapter extends BaseAdapter {

    private List<PromoteRankBean> list;
    private int page;

    public PromoteRankAdapter(int page) {
        this.page = page;
    }

    public void setData(List<PromoteRankBean> list) {
        this.list = list;
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
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_promote_ranking, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.name.setText(list.get(i).getSpread());//推广员姓名
        viewHolder.promoteNum.setText(list.get(i).getShopnum());//推广人数
        viewHolder.orderNum.setText(list.get(i).getNum());//订单总数
        viewHolder.orderTotal.setText(list.get(i).getSettAmount());//订单金额
        return view;
    }



    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.promote_num)
        TextView promoteNum;
        @BindView(R.id.order_num)
        TextView orderNum;
        @BindView(R.id.order_total)
        TextView orderTotal;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
