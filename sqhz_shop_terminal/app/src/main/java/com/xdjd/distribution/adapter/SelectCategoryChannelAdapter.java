package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddInfoBean;
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

public class SelectCategoryChannelAdapter extends BaseAdapter {

    private ItemOnListener listener;

    public int index = 0;

    public List<AddInfoBean> list;

    private String type;

    public void setData(List<AddInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public SelectCategoryChannelAdapter(ItemOnListener listener, String type) {
        this.listener = listener;
        this.type = type;
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
                    inflate(R.layout.item_select_category_channel, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ("1".equals(type)) {//类别
            holder.mNameTv.setText(list.get(i).getCc_name());
        } else if ("2".equals(type)) {//渠道
            holder.mNameTv.setText(list.get(i).getCct_name());
        }


        if (index == i) {
//            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_blue));
            holder.mLlMain.setBackgroundResource(R.color.color_yellow);
        } else {
//            holder.mNameTv.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mLlMain.setBackgroundResource(R.color.white);
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
        @BindView(R.id.ll_main)
        LinearLayout mLlMain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
