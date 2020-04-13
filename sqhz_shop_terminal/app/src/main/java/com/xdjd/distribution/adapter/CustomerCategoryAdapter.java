package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddInfoBean;
import com.xdjd.distribution.bean.LineBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/26.
 * 客户类别
 */

public class CustomerCategoryAdapter extends BaseAdapter {

    private List<AddInfoBean> list;
    private ItemOnListener listener;
    private String categoryId = "";

    public void setData(List<AddInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public CustomerCategoryAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setId(String categoryId){
        this.categoryId = categoryId;
    }

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
                    inflate(R.layout.item_select_name, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tvName.setText(list.get(i).getCc_name());

        if (categoryId.equals(list.get(i).getCc_id())) {
            holder.tvName.setTextColor(UIUtils.getColor(R.color.text_blue));
        } else {
            holder.tvName.setTextColor(UIUtils.getColor(R.color.text_gray));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
