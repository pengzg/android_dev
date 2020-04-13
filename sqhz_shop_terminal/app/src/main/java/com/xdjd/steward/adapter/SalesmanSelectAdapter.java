package com.xdjd.steward.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.steward.bean.CategoryBean;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/26.
 * 员工选择adapter
 */

public class SalesmanSelectAdapter extends BaseAdapter {

    private List<SalesdocListBean> list;
    private String suId = "";// 选中的位置

    private ItemOnListener listener;

    public SalesmanSelectAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<SalesdocListBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holde;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_name, parent,false);
            holde = new ViewHolder(convertView);
            convertView.setTag(holde);
        } else {
            holde = (ViewHolder) convertView.getTag();
        }

        holde.mTvName.setText(list.get(position).getSu_name());
        if (list.get(position).getSu_id().equals(suId)) {
            holde.mTvName.setTextColor(UIUtils.getColor(R.color.text_blue));
        } else {
            holde.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItem(position);
            }
        });
        return convertView;
    }

    public void setSalesmanId(String suId) {
        this.suId = suId;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
