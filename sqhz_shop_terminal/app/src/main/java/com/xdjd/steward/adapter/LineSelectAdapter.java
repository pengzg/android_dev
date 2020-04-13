package com.xdjd.steward.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/15 0015.
 * 线路选择adapter
 */
public class LineSelectAdapter extends BaseAdapter {
    private List<LineBean> mDatas;
    private String lineId = "";// 选中的位置

    private ItemOnListener listener;

    public LineSelectAdapter(List<LineBean> datas,ItemOnListener listener) {
        this.mDatas = datas;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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

        holde.mTvName.setText(mDatas.get(position).getBl_name());
        if (lineId.equals(mDatas.get(position).getBl_id())) {
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

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
