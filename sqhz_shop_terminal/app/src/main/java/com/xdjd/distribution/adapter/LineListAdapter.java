package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/26.
 */

public class LineListAdapter extends BaseAdapter {

    private List<LineBean> listName;
    private ItemOnListener listener;
    private String lineId = "";

    public LineListAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setId(String lineId) {
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
                    inflate(R.layout.item_line_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvName.setText(listName.get(i).getBl_name());

        holder.mRlDel.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.rl_del)
        RelativeLayout mRlDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
