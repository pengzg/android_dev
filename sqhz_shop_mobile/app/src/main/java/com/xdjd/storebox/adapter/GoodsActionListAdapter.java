package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.GoodsDetailBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2016/12/30.
 */

public class GoodsActionListAdapter extends BaseAdapter {

    /*List<GoodsDetailBean.GoodsActionBean> list;

    public GoodsActionListAdapter(List<GoodsDetailBean.GoodsActionBean> list) {
        this.list = list;
    }

    public void setData(List<GoodsDetailBean.GoodsActionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }*/

    @Override
    public int getCount() {
        return 1/*list == null ? 0 : list.size()*/;
    }

    @Override
    public Object getItem(int i) {
        return 1/*list.get(i)*/;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view==null){
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_goods_action_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            AutoUtils.auto(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        //holder.mName.setText(list.get(i).getGoodsName());
        //holder.mDescribe.setText(list.get(i).getActivityGoodsDesc());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.describe)
        TextView mDescribe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
