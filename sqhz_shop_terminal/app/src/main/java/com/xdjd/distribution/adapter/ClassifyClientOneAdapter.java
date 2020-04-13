package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.LineClientBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class ClassifyClientOneAdapter extends BaseAdapter {

    private ItemOnListener listener;

    private int index = 0;
    List<LineClientBean> list;

    public int getIndex() {
        return index;
    }

    public void setData(List<LineClientBean> list) {
        this.list = list;
        index = 0;
        notifyDataSetChanged();
    }

    public ClassifyClientOneAdapter(ItemOnListener listener) {
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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_classify_client_one, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mNameTv.setText(list.get(i).getCc_name() + "(" + list.get(i).getCount() + ")");

        if (index == i) {
            holder.mLlMain.setBackgroundResource(R.color.color_yellow);
            //            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            //            holder.mLine.setVisibility(View.VISIBLE);
        } else {
            holder.mLlMain.setBackgroundResource(R.color.white);
            //            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            //            holder.mLine.setVisibility(View.INVISIBLE);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                notifyDataSetChanged();
                listener.onItem(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        //        @BindView(R.id.line)
        //        View mLine;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
