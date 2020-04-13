package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.ShopAuditBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lijipei on 2017/10/28.
 * 店铺信息列表
 */

public class AuditPrizeAdapter extends BaseAdapter {

    List<ShopAuditBean> list;
    private ItemOnListener listener;

    public AuditPrizeAdapter(ItemOnListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit_prize, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShopAuditBean bean = list.get(position);
        holder.mTvName.setText(bean.getShopname());
        holder.mTvGoodsName.setText(bean.getProductname());
        holder.mTvTotal.setText("总数量:"+bean.getZsnum() + bean.getUnit());
        holder.mTvCheck.setText("已结算:"+bean.getYjsnum() + bean.getUnit());
        holder.mTvNoCheck.setText("未结算:"+bean.getWjsnum() + bean.getUnit());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(position);
            }
        });
        return convertView;
    }

    public void setData(List<ShopAuditBean> listShopGoodsAudit) {
        this.list = listShopGoodsAudit;
        notifyDataSetInvalidated();
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_total)
        TextView mTvTotal;
        @BindView(R.id.tv_check)
        TextView mTvCheck;
        @BindView(R.id.tv_no_check)
        TextView mTvNoCheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
