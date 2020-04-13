package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.MessageBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/7/19.
 */

public class MessageAdapter extends BaseAdapter {
    public List<MessageBean> list;

    public void setData(List<MessageBean> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mMessageTitle.setText(list.get(i).getMp_title());
        holder.mMessageDate.setText(list.get(i).getMp_add_time());
        holder.mMessageDescribe.setText(list.get(i).getMp_content());

        //0 未读 1 已读
        if ("0".equals(list.get(i).getMp_read_flag())){
            holder.mIvOval.setVisibility(View.VISIBLE);
        }else{
            holder.mIvOval.setVisibility(View.GONE);
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.message_title)
        TextView mMessageTitle;
        @BindView(R.id.message_date)
        TextView mMessageDate;
        @BindView(R.id.message_describe)
        TextView mMessageDescribe;
        @BindView(R.id.iv_oval)
        ImageView mIvOval;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
