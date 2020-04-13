package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/18.
 */

public class BalanceAdapter extends BaseAdapter {
    private incomeAndExpenseListener listener;

    public BalanceAdapter(incomeAndExpenseListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_me_balance, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.item();
            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.pay_type)
        TextView payType;
        @BindView(R.id.pay_time)
        TextView payTime;
        @BindView(R.id.balance)
        TextView balance;
        @BindView(R.id.pay_num)
        TextView payNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    public interface incomeAndExpenseListener{
        public void item();
    }
}
