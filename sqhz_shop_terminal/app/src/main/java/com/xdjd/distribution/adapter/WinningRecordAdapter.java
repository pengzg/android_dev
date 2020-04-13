package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.WinningListBean;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 兑奖记录列表
 */

public class WinningRecordAdapter extends BaseAdapter {

    List<WinningListBean> list;

    public WinningRecordAdapter(List<WinningListBean> list) {
        this.list = list;
    }

    public void setData(List<WinningListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_winning_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!"".equals(list.get(position).getMw_memberid_image()) && null != list.get(position).getMw_memberid_image()){
            Glide.with(parent.getContext()).load(list.get(position).getMw_memberid_image()).into(holder.mCsImage);
        }

        holder.mTvName.setText(list.get(position).getMw_memberid_nameref());
        holder.mTvDate.setText(list.get(position).getMw_receivetime());
        holder.mTvDesc.setText(list.get(position).getProductname() + "x" + list.get(position).getMw_num() + list.get(position).getUnit());

        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.cs_image)
        CircleImageView mCsImage;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;
        @BindView(R.id.tv_date)
        TextView mTvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
