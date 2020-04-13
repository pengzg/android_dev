package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.TaskBean;
import com.xdjd.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class DistributionCategoryAdapter extends BaseAdapter {

    private DistributionCategoryListener listener;
    public int index = 0;

    List<TaskBean> list;

    public void setData(List<TaskBean> list) {
        this.list = list;
        index = 0;
        notifyDataSetChanged();
    }

    public int getIndex() {
        return index;
    }

    public DistributionCategoryAdapter(DistributionCategoryListener listener) {
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
                    inflate(R.layout.item_distribution_category, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (index == i){
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.white));
            holder.mTvName.setBackgroundColor(UIUtils.getColor(R.color.color_blue_tinge));
        }else{
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
            holder.mTvName.setBackgroundColor(UIUtils.getColor(R.color.white));
        }

        holder.mTvName.setText(list.get(i).getOm_salesid_nameref()+"("+list.get(i).getCount()+")");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                notifyDataSetChanged();
                listener.onItemCategory(i);
            }
        });
        return view;
    }

    public interface DistributionCategoryListener{
        void onItemCategory(int i);
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
