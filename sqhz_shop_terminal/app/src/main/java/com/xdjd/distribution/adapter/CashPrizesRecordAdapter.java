package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.WinningListBean;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 核销记录adapter
 */

public class CashPrizesRecordAdapter extends BaseAdapter {

    List<WinningListBean> list;
    private int mwState = 1;//1未核销 2已核销

    public void setMwState(int mwState) {
        this.mwState = mwState;
    }

    public CashPrizesRecordAdapter(List<WinningListBean> list) {
        this.list = list;
    }

    public void setData(List<WinningListBean> list) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_prizes_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!"".equals(list.get(position).getMw_memberid_image()) && null != list.get(position).getMw_memberid_image()){
            Glide.with(parent.getContext()).load(list.get(position).getMw_memberid_image()).into(holder.mCsImage);
        }

        holder.mTvCustomerName.setText(list.get(position).getMw_memberid_nameref());
        holder.mTvGoodsName.setText(list.get(position).getProductname());

        if (mwState == 1) {//未核销
            holder.mTvShopName.setText(list.get(position).getMw_shopid_nameref());

            holder.mTvDateStatus.setText("中奖时间: ");
            holder.mTvDate.setText(list.get(position).getMw_winningtime());
        } else {//已核销
            holder.mTvShopName.setText(list.get(position).getShopname());

            holder.mTvDateStatus.setText("领奖时间: ");
            holder.mTvDate.setText(list.get(position).getMw_receivetime());
        }

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_shop_name)
        TextView mTvShopName;
        @BindView(R.id.tv_customer_name)
        TextView mTvCustomerName;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_date_status)
        TextView mTvDateStatus;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.cs_image)
        CircleImageView mCsImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
