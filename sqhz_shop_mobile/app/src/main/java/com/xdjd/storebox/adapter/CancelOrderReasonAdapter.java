package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.CancelOrderReasonBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/8/17.
 */

public class CancelOrderReasonAdapter extends BaseAdapter {
    private List<CancelOrderReasonBean> list;
    private itemListener listener;
    private int  flagI = -1;

    public CancelOrderReasonAdapter(itemListener listener) {
        this.listener = listener;
    }

    public void setData(List<CancelOrderReasonBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void setFlag(int i){
        this.flagI = i;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cancel_order_reason, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if(i == flagI){
            viewHolder.image.setSelected(true);
        }else{
            viewHolder.image.setSelected(false);
        }
        viewHolder.cancelDesc.setText(list.get(i).getCancelMsg());//取消原因描述
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(viewHolder.image.isSelected()){
                    viewHolder.image.setSelected(false);
                }else{
                    viewHolder.image.setSelected(true);
                }*/
                listener.item(i,list.get(i).getCancelId());
            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.cancelDesc)
        TextView cancelDesc;
        @BindView(R.id.ll_reason)
        LinearLayout llReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface itemListener{
        void item(int i,String cancelId);
    }
}
