package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.steward.bean.ShopWinningDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 店铺信息列表
 */

public class ShopWinningRecordAdapter extends BaseAdapter {

    List<ShopWinningDetailBean> list;
    private int mWinningType = 1;

    public void setData(List<ShopWinningDetailBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_winning_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopWinningDetailBean bean = list.get(position);

        if (mWinningType == 1){//店铺
            holder.mLlGoods.setVisibility(View.VISIBLE);

            holder.mTvShopDesc.setText("店铺名称:");
            holder.mTvShopName.setText(bean.getShopname());
            holder.mTvGoodsName.setText(bean.getProductname());
        }else{//商品
            holder.mTvShopDesc.setText("商品名称:");
            holder.mTvShopName.setText(bean.getProductname());

            holder.mLlGoods.setVisibility(View.GONE);

        }

        holder.mTvTotalNum.setText("中奖总数量:" + bean.getTotal_nums());
        holder.mTvYdnum.setText("已兑奖:" + bean.getYdnum());
        holder.mTvWdnum.setText("未兑奖:" + bean.getWdnum());

        holder.mTvZsnum.setText("总数量:" + bean.getZsnum());
        holder.mTvYjsnum.setText("已结算:" + bean.getYjsnum());
        holder.mTvWjsnum.setText("未结算:" + bean.getWjsnum());

        return convertView;
    }

    public void setType(int type) {
        this.mWinningType = type;
    }

    class ViewHolder {
        @BindView(R.id.tv_shop_name)
        TextView mTvShopName;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_total_num)
        TextView mTvTotalNum;
        @BindView(R.id.tv_zsnum)
        TextView mTvZsnum;
        @BindView(R.id.tv_ydnum)
        TextView mTvYdnum;
        @BindView(R.id.tv_yjsnum)
        TextView mTvYjsnum;
        @BindView(R.id.tv_wdnum)
        TextView mTvWdnum;
        @BindView(R.id.tv_wjsnum)
        TextView mTvWjsnum;
        @BindView(R.id.tv_shop_desc)
        TextView mTvShopDesc;
        @BindView(R.id.ll_title)
        LinearLayout mLlTitle;
        @BindView(R.id.ll_goods)
        LinearLayout mLlGoods;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
