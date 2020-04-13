package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.StatisticListBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/1/4.
 */

public class PromoteStatisticAdapter extends BaseAdapter {
    private promoteListener listener;
    private List<StatisticListBean> list;
    private int page;
    public PromoteStatisticAdapter(promoteListener listener,int page) {
        this.listener = listener;
        this.page = page;
    }
    public void setData(List<StatisticListBean> list){
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_promote_order, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.item_promote(list.get(i).getOrderId());
            }
        });
        viewHolder.week.setText(list.get(i).getWeek());//星期几
        viewHolder.time.setText(list.get(i).getAddTime());//时间
        Glide.with(viewGroup.getContext()).load(list.get(i).getAvatar()).into(viewHolder.image);//图像
        viewHolder.totalAccount.setText(list.get(i).getSettlementAmount());//订单金额
        viewHolder.shopName.setText(list.get(i).getShopName());//中心仓名字
        viewHolder.orderStatus.setText(list.get(i).getOrderStatus_nameref());//订单状态
        return view;
    }



    static class ViewHolder {
        @BindView(R.id.week)
        TextView week;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.total_account)
        TextView totalAccount;
        @BindView(R.id.shop_name)
        TextView shopName;
        @BindView(R.id.order_status)
        TextView orderStatus;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface promoteListener {
        public void item_promote(String orderId);
    }


}
