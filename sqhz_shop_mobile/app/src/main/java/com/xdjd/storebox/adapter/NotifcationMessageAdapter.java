package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.MessageBean;
import com.xdjd.utils.UIUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/2/21.
 */

public class NotifcationMessageAdapter extends BaseAdapter {

    private List<MessageBean> list;
    private NotifcationMessageListener listener;

    public NotifcationMessageAdapter(NotifcationMessageListener listener) {
        this.listener = listener;
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notifcation_message, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //是否过期	1未过期 2 已过期
        if ("2".equals(list.get(i).getMessage_is_expired())) {
            holder.mMessageDate.setTextColor(UIUtils.getColor(R.color.color_BFBFBF));
            holder.messageTitle.setTextColor(UIUtils.getColor(R.color.color_BFBFBF));
            holder.messageContent.setTextColor(UIUtils.getColor(R.color.color_BFBFBF));
            holder.mOverprintOverImg.setVisibility(View.VISIBLE);
        } else {
            holder.mMessageDate.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.messageTitle.setTextColor(UIUtils.getColor(R.color.text_212121));
            holder.messageContent.setTextColor(UIUtils.getColor(R.color.color_5B5B5B));
            holder.mOverprintOverImg.setVisibility(View.GONE);
        }

        holder.mMessageDate.setText(list.get(i).getMessage_time());
        holder.messageTitle.setText(list.get(i).getMessage_title());
        holder.messageContent.setText(list.get(i).getMessage_content());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemListener(i);
            }
        });

        return view;
    }

    public void setData(List<MessageBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @BindView(R.id.message_title)
        TextView messageTitle;
        @BindView(R.id.message_content)
        TextView messageContent;
        @BindView(R.id.message_date)
        TextView mMessageDate;
        @BindView(R.id.overprint_over_img)
        ImageView mOverprintOverImg;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface NotifcationMessageListener {
        void itemListener(int position);
    }
}
