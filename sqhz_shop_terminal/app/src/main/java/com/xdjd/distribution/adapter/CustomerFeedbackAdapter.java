package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.FeedbackBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerFeedbackAdapter extends BaseAdapter {

    List<FeedbackBean> list;

    OnCustomerFeedbackListener listener;

    public void setData(List<FeedbackBean> list){
        this.list = list;
        notifyDataSetInvalidated();
    }

    public CustomerFeedbackAdapter(OnCustomerFeedbackListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_customer_feedback, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mLlReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReply(i);
            }
        });

        return view;
    }

    public interface OnCustomerFeedbackListener {
        void onReply(int i);//回复
    }

    class ViewHolder {
        @BindView(R.id.iv_reply)
        ImageView mIvReply;
        @BindView(R.id.ll_reply)
        LinearLayout mLlReply;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
