package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.FeedbackBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/1/16.
 */

public class FeedbackReplayAdapter extends BaseAdapter {
    private List<FeedbackBean> list;

    public void setData(List<FeedbackBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feedback_replay, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.meQustion.setText(list.get(i).getCf_content());
        viewHolder.status.setText("["+list.get(i).getCf_process_status_nameref()+"]");
        if("".equals(list.get(i).getCf_process_note())|| list.get(i).getCf_process_note() == null){
            viewHolder.boxReplay.setText("还没有回复，请您耐心等待！");
            viewHolder.replyTime.setVisibility(View.GONE);
        }else {
            viewHolder.boxReplay.setText(list.get(i).getCf_process_note());
            viewHolder.replyTime.setVisibility(View.VISIBLE);
        }
        viewHolder.addTime.setText(list.get(i).getCf_addtime());
        return view;
    }



    static class ViewHolder {
        @BindView(R.id.me_qustion)
        TextView meQustion;
        @BindView(R.id.box_replay)
        TextView boxReplay;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.first_line)
        View firstLine;
        @BindView(R.id.add_time)
        TextView addTime;
        @BindView(R.id.reply_time)
        TextView replyTime;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
