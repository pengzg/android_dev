package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.VisitingAlarmBean;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/7/1.
 */

public class VisitingAlarmAdapter extends BaseAdapter {

    public List<VisitingAlarmBean> list;

    /**
     * 是否是提醒,true:提醒;false:超期;
     */
    private boolean isAlarm = false;

    public VisitingAlarmAdapter(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }

    public void setData(List<VisitingAlarmBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visiting_alarm, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText(position+1+"");
        holder.mTvName.setText(list.get(position).getCc_name());
        holder.mTvCustomerName.setText(list.get(position).getCc_contacts_name());
        holder.mTvMobile.setText(list.get(position).getCc_contacts_mobile());
        holder.mTvAddress.setText(list.get(position).getCc_address());

        holder.mTvLastVisitingDate.setText(list.get(position).getCc_last_visiting_date());
        if (isAlarm){
            holder.mTvLastVisitDays.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvLastVisitDays.setText(list.get(position).getLast_visit_days()+"天前到访");
        }else{
            holder.mTvLastVisitDays.setTextColor(UIUtils.getColor(R.color.text_dark_red));
            holder.mTvLastVisitDays.setText(list.get(position).getLast_visit_days()+"天前到访,已超期"+list.get(position).getExpire_days()+"天");
        }
        holder.mTvVisitingCycle.setText(list.get(position).getCc_visiting_cycle());
        return view;
    }


    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_last_visiting_date)
        TextView mTvLastVisitingDate;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_last_visit_days)
        TextView mTvLastVisitDays;
        @BindView(R.id.tv_mobile)
        TextView mTvMobile;
        @BindView(R.id.tv_visiting_cycle)
        TextView mTvVisitingCycle;
        @BindView(R.id.tv_address)
        TextView mTvAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
