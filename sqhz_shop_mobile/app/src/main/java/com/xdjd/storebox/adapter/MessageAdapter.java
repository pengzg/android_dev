package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.MessageBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/2/21.
 */

public class MessageAdapter extends BaseAdapter {

    private MessageListener listener;
    private List<MessageBean> list;

    public MessageAdapter(MessageListener listener) {
        this.listener = listener;
    }

    public void setData(List<MessageBean> listData) {
        this.list = listData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0:list.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_message, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load(list.get(i).getMessage_icon()).
                placeholder(R.color.image_gray).into(holder.mMessageImg);
        holder.mMessageTitle.setText(list.get(i).getMessage_type_nameref());
        holder.mMessageDescribe.setText(list.get(i).getNew_message_describe());
        holder.mMessageDate.setText(list.get(i).getNew_message_time());

        if (!"".equals(list.get(i).getMessage_unread_num()) || list.get(i).getMessage_unread_num()!=null){
            if ("0".equals(list.get(i).getMessage_unread_num())){
                holder.mMessageNumRl.setVisibility(View.GONE);
            }else {
                holder.mMessageNumRl.setVisibility(View.VISIBLE);
            }
        }else{
            holder.mMessageNumRl.setVisibility(View.GONE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemListener(i);
            }
        });

        return view;
    }

    public interface MessageListener {
        void itemListener(int position);
    }

    class ViewHolder {
        @BindView(R.id.message_img)
        ImageView mMessageImg;
        @BindView(R.id.message_num_rl)
        RelativeLayout mMessageNumRl;
//        @BindView(R.id.message_num_tv)
//        TextView mMessageNumTv;
        @BindView(R.id.message_title)
        TextView mMessageTitle;
        @BindView(R.id.message_date)
        TextView mMessageDate;
        @BindView(R.id.message_describe)
        TextView mMessageDescribe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
