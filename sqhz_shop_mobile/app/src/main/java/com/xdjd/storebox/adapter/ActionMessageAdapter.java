package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.MessageBean;
import com.xdjd.utils.UIUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 活动消息adapter
 * Created by lijipei on 2017/2/21.
 */

public class ActionMessageAdapter extends BaseAdapter {

    ActionMessageListener listener;
    private List<MessageBean> list;

    public ActionMessageAdapter(ActionMessageListener listener) {
        this.listener = listener;
    }

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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_action_message, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //是否过期	1未过期 2 已过期
        if ("2".equals(list.get(i).getMessage_is_expired())) {
            holder.mMessageTitle.setTextColor(UIUtils.getColor(R.color.color_BFBFBF));
            holder.mMessageContent.setTextColor(UIUtils.getColor(R.color.color_BFBFBF));
            holder.mActionEndTv.setVisibility(View.VISIBLE);
        } else {
            holder.mMessageTitle.setTextColor(UIUtils.getColor(R.color.text_212121));
            holder.mMessageContent.setTextColor(UIUtils.getColor(R.color.color_5B5B5B));
            holder.mActionEndTv.setVisibility(View.GONE);
        }

        holder.mMessageTitle.setText(list.get(i).getMessage_title());
        holder.mMessageContent.setText(list.get(i).getMessage_content());
        holder.mMessageDate.setText(list.get(i).getMessage_time());

        Glide.with(viewGroup.getContext()).
                load(list.get(i).getMessage_img_nameref()).placeholder(R.color.image_gray).into(holder.mMessageImg);

        holder.mActionMessageLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemListener(i);
            }
        });

        return view;
    }

    public interface ActionMessageListener {
        void itemListener(int position);
    }

    class ViewHolder {
        @BindView(R.id.message_date)
        TextView mMessageDate;
        @BindView(R.id.message_title)
        TextView mMessageTitle;
        @BindView(R.id.message_img)
        ImageView mMessageImg;
        @BindView(R.id.message_content)
        TextView mMessageContent;
        @BindView(R.id.action_message_ll)
        LinearLayout mActionMessageLl;
        @BindView(R.id.action_end_tv)
        TextView mActionEndTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
