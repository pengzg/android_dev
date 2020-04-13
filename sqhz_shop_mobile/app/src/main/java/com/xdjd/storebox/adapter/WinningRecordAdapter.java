package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.CustomerWinningBean;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 */

public class WinningRecordAdapter extends BaseAdapter {

    List<CustomerWinningBean> list;

    public WinningRecordAdapter(List<CustomerWinningBean> list) {
        this.list = list;
    }

    public void setData(List<CustomerWinningBean> list) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_winning_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!"".equals(list.get(position).getMw_member_mb_img()) && null != list.get(position).getMw_member_mb_img()){
            Glide.with(parent.getContext()).load(list.get(position).getMw_member_mb_img()).into(holder.mCsImage);
        }

        holder.mTvActionName.setText(list.get(position).getMw_activity_name());
        holder.mTvName.setText(list.get(position).getMw_member_name());
        holder.mTvDate.setText(list.get(position).getMw_granttime());
        holder.mTvDesc.setText(/*list.get(position).getMw_productname() + "x" + */list.get(position).getMw_nums_desc());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.cs_image)
        CircleImageView mCsImage;
        @BindView(R.id.tv_action_name)
        TextView mTvActionName;
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
