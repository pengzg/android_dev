package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.GoodClassifyListBean;
import com.xdjd.utils.UIUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 商品二级分类adapter
 * Created by lijipei on 2016/11/21.
 */

public class ClassifyTwoAdapter extends BaseAdapter {

    private ClassifyTwoListener listener;
    private int classifyIndex = 0;
    private List<GoodClassifyListBean> list;

    public ClassifyTwoAdapter(ClassifyTwoListener listener) {
        this.listener = listener;
    }

    public void setData(List<GoodClassifyListBean> list){
        this.list = list;
        this.classifyIndex = 0;
        notifyDataSetChanged();
    }

    public void setIndex(int classifyIndex) {
        this.classifyIndex = classifyIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null?0:list.size();
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
                    inflate(R.layout.item_classify_two, viewGroup, false);
            holder = new ViewHolder(view);

            view.setTag(holder);
            //对于listview，注意添加这一行，即可在item上使用高度
            AutoUtils.autoSize(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mItemCtTv.setText(list.get(i).getGcName());

        if (classifyIndex == i) {
            holder.mItemCtIv.setVisibility(View.VISIBLE);
            holder.mItemCtLl.setBackgroundColor(UIUtils.getColor(R.color.white));
            holder.mItemCtTv.setSelected(true);
        } else {
            holder.mItemCtIv.setVisibility(View.INVISIBLE);
            holder.mItemCtLl.setBackgroundColor(UIUtils.getColor(R.color.color_e6e6e6));
            holder.mItemCtTv.setSelected(false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClassifyTwo(i);
            }
        });

        return view;
    }

    public interface ClassifyTwoListener {
        void itemClassifyTwo(int position);
    }

    class ViewHolder {
        @BindView(R.id.item_ct_tv)
        TextView mItemCtTv;
        @BindView(R.id.item_ct_iv)
        ImageView mItemCtIv;
        @BindView(R.id.item_ct_ll)
        LinearLayout mItemCtLl;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
