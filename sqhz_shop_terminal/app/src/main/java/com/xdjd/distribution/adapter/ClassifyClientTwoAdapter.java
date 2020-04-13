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

public class ClassifyClientTwoAdapter extends BaseAdapter {

    private ItemTwoOnListener listener;

    public int index = 0;
    List<LineClientBean> list;

    public void setData(List<LineClientBean> list) {
        this.list = list;
        index = 0;
        notifyDataSetChanged();
    }

    public ClassifyClientTwoAdapter(ItemTwoOnListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list == null ? 0:list.size();
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
                    inflate(R.layout.item_classify_client_two, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mNameTv.setText(list.get(i).getCc_name()+"("+list.get(i).getCount()+")");

        if (index == i) {
            holder.mLlMain.setBackgroundResource(R.color.color_yellow);
        } else {
            holder.mLlMain.setBackgroundResource(R.color.white);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                notifyDataSetChanged();
                listener.onItemTwo(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemTwoOnListener{
        void onItemTwo(int position);
    }
}
