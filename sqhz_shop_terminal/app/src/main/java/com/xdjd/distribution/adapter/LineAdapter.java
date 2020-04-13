package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/26.
 */

public class LineAdapter extends BaseAdapter {

    private List<LineBean> listName;
    private ItemOnListener listener;
    private String lineId = "";

    public LineAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setId(String lineId){
        this.lineId = lineId;
    }

    public int getCount() {
        return listName == null ? 0 : listName.size();
    }

    @Override
    public Object getItem(int i) {
        return listName.get(i);
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
                    inflate(R.layout.item_line, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.linesTv.setText(listName.get(i).getBl_name());

        if (listName.get(i).getBl_id().equals(lineId)) {
            holder.mLinesIv.setVisibility(View.VISIBLE);
            holder.linesTv.setTextColor(UIUtils.getColor(R.color.text_blue));
        } else {
            holder.mLinesIv.setVisibility(View.GONE);
            holder.linesTv.setTextColor(UIUtils.getColor(R.color.text_gray));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    public void setData(List<LineBean> list) {
        this.listName = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.lines_tv)
        TextView linesTv;
        @BindView(R.id.lines_iv)
        ImageView mLinesIv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
