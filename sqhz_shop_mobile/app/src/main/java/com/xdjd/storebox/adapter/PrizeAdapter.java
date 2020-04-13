package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.GiftWinningBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 奖品兑换记录
 */

public class PrizeAdapter extends BaseAdapter {

    List<GiftWinningBean> list;

    public PrizeAdapter(List<GiftWinningBean> list) {
        this.list = list;
    }

    public void setData(List<GiftWinningBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prize_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvName.setText(list.get(position).getMw_productname());
        holder.mTvWinningNum.setText(list.get(position).getMw_nums_desc());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_winning_num)
        TextView mTvWinningNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
