package com.xdjd.winningrecord.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.winningrecord.bean.ActivityListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/11/2.
 */

public class PrizeActionAdapter extends BaseAdapter {
    List<ActivityListBean> list;

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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prize_action, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(parent.getContext()).load(list.get(position).
                getBackground_image()).error(R.color.image_gray).into(holder.mIvImg);

        holder.mTvActionName.setText(list.get(position).getMm_name());
        holder.mTvTypeName.setText(list.get(position).getMm_type_nameref());

        holder.mTvPrizenumDesc.setText(list.get(position).getMm_prize_grantnum() + "/" + list.get(position).getMm_prize_currentnum());
        holder.mTvJoinNumDesc.setText(list.get(position).getMm_participant_num() + "/" + list.get(position).getMm_participant_num_max());

        holder.mTvSpreadNum.setText(list.get(position).getMm_spread_num());//传播人数
        holder.mTvState.setText(list.get(position).getArea_range());
        holder.mTvActionTime.setText(list.get(position).getMm_startime() + "~" + list.get(position).getMm_endtime());

        return convertView;
    }

    public void setData(List<ActivityListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_action_name)
        TextView mTvActionName;
        @BindView(R.id.tv_type_name)
        TextView mTvTypeName;
        @BindView(R.id.tv_prizenum_desc)
        TextView mTvPrizenumDesc;
        @BindView(R.id.tv_join_num_desc)
        TextView mTvJoinNumDesc;
        @BindView(R.id.tv_spread_num)
        TextView mTvSpreadNum;
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_action_time)
        TextView mTvActionTime;
        @BindView(R.id.iv_img)
        ImageView mIvImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
