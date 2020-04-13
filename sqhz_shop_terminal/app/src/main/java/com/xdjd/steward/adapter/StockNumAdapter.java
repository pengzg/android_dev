package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.steward.bean.GoodsStockBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/7/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StockNumAdapter extends BaseAdapter {

    List<GoodsStockBean> list;

    public void setData(List<GoodsStockBean> list) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stock_num, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mTvIndex.setText((i+1)+"");
        holder.mTvStockName.setText(list.get(i).getGgs_storehouseid_nameref());//仓库名称
        holder.mTvGoodsName.setText(list.get(i).getGg_title()+list.get(i).getGg_model());//商品名称
        holder.mTvStockNum.setText(list.get(i).getGgp_stock_desc());

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_stock_name)
        TextView mTvStockName;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_stock_num)
        TextView mTvStockNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
