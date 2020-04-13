package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.OrderGoodsDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片适配器
 * Created by lijipei on 2016/12/10.
 */

public class GoodsImageAdapter extends BaseAdapter {

    private List<OrderGoodsDetailBean> list;

    public void setData(List<OrderGoodsDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_goods_image,
                    viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Glide.with(viewGroup.getContext()).load(list.get(i).getBpa_path()).
                into(holder.mGoodsIv);
        //holder.mGoodsNumTv.setText(list.get(i).getGoodsNum());

        return view;
    }


    class ViewHolder {
        @BindView(R.id.goods_iv)
        ImageView mGoodsIv;
        @BindView(R.id.goods_num_tv)
        TextView mGoodsNumTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
