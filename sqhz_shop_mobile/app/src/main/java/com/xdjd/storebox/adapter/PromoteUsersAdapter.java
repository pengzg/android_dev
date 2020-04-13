package com.xdjd.storebox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.PromoteUserBean;
import com.xdjd.view.CircleImageView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/1/3.
 */

public class PromoteUsersAdapter extends BaseAdapter {
    private userListener listener;
    private int page;
    private List<PromoteUserBean> list;
    public PromoteUsersAdapter(userListener listener,int page ) {
        this.listener = listener;
        this.page = page;
    }
    public void setData(List<PromoteUserBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        //return 20;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_promote_users, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);//百分比处理
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.dialPhone(viewHolder.promoteUserMobile.getText().toString());
            }
        });
        Glide.with(viewGroup.getContext()).load(list.get(i).getAvatar()).into(viewHolder.imageUser);
        viewHolder.shopName.setText(list.get(i).getShopName());
        viewHolder.promoteTime.setText(list.get(i).getSpreadTime());
        viewHolder.promoteUserName.setText(list.get(i).getNickname());
        viewHolder.promoteUserMobile.setText(list.get(i).getMobile());
        viewHolder.orderTotal.setText(list.get(i).getOrderCount());
        viewHolder.accountTotal.setText(list.get(i).getTotalAmount());
        viewHolder.shopAddress.setText(list.get(i).getAddress());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.image_user)
        CircleImageView imageUser;
        @BindView(R.id.shop_name)
        TextView shopName;
        @BindView(R.id.promote_time)
        TextView promoteTime;
        @BindView(R.id.promote_user_name)
        TextView promoteUserName;
        @BindView(R.id.promote_user_mobile)
        TextView promoteUserMobile;
        @BindView(R.id.order_total)
        TextView orderTotal;
        @BindView(R.id.account_total)
        TextView accountTotal;
        @BindView(R.id.shop_address)
        TextView shopAddress;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface userListener{
        public void dialPhone(String mobile);
    }
}
