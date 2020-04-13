package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.TaskListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/7/1.
 */

public class VisitingListAdapter extends BaseAdapter {

    public List<TaskListBean> list;

    public void setData(List<TaskListBean> list) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visiting_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText(position + 1 + "");
        holder.mTvName.setText(list.get(position).getClct_customerid_nameref());
        holder.mTvCustomerName.setText(list.get(position).getContact_name());
        holder.mTvMobile.setText(list.get(position).getClct_customerid_mobile());
        holder.mTvAddress.setText(list.get(position).getClct_customerid_address());

        holder.mTvLineName.setText(list.get(position).getBl_name());

        holder.mTvTraveltime.setText("在途时间:"+list.get(position).getClct_traveltime());
        holder.mTvServiceTime.setText("服务时间:"+list.get(position).getService_time());

        holder.mTvArrivetime.setText(list.get(position).getClct_arrivetime());
        holder.mTvLeavetime.setText(list.get(position).getClct_leavetime());
        return view;
    }


    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_mobile)
        TextView mTvMobile;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.tv_line_name)
        TextView mTvLineName;

        @BindView(R.id.tv_traveltime)
        TextView mTvTraveltime;
        @BindView(R.id.tv_service_time)
        TextView mTvServiceTime;
        @BindView(R.id.tv_arrivetime)
        TextView mTvArrivetime;
        @BindView(R.id.tv_leavetime)
        TextView mTvLeavetime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
