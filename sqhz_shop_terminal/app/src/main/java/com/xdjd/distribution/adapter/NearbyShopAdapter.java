package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.distribution.bean.NearbyShopBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by freestyle_hong on 2017/9/22.
 */

public class NearbyShopAdapter extends BaseAdapter {
    private List<NearbyShopBean> list;
    private ItemOnListener listener;
    private Integer type;

    public NearbyShopAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

    public void setData(List<NearbyShopBean> list,int type){
        this.list = list;
        this.type = type;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return null == list ? 0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearby_shop, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        if(!"".equals(list.get(i).getCcl_img())&& null!= list.get(i).getCcl_img()){
            Glide.with(viewGroup.getContext()).load(list.get(i).getCcl_img()).into(viewHolder.csImage);
        }else{
            viewHolder.csImage.setImageResource(R.mipmap.customer_img);
        }
        viewHolder.tvName.setText(list.get(i).getCcl_name());
        viewHolder.tvContactsName.setText(list.get(i).getCcl_contacts_name());
        viewHolder.tvMobile.setText(list.get(i).getCcl_contacts_mobile());
        viewHolder.tvAddress.setText(list.get(i).getCcl_address());
        if(type == 1){
            viewHolder.distance.setVisibility(View.VISIBLE);
            viewHolder.checkStatus.setVisibility(View.GONE);
            if(list.get(i).getDistance() != null&&!"".equals(list.get(i).getDistance())&&Integer.valueOf(list.get(i).getDistance()) > 1000){
                viewHolder.distance.setText(Integer.valueOf(list.get(i).getDistance())/1000 + "公里");
            }else{
                viewHolder.distance.setText(list.get(i).getDistance()+"米");
            }
        }else if(type == 3){//已采集店铺
            viewHolder.distance.setVisibility(View.GONE);
            viewHolder.checkStatus.setVisibility(View.VISIBLE);
            viewHolder.checkStatus.setText(list.get(i).getCcl_addtime());
        }else{
            viewHolder.distance.setVisibility(View.GONE);
            viewHolder.checkStatus.setVisibility(View.GONE);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItem(i);
            }
        });
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.cs_image)
        CircleImageView csImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_contacts_name)
        TextView tvContactsName;
        @BindView(R.id.tv_mobile)
        TextView tvMobile;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_location)
        ImageView ivLocation;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.check_status)
        TextView checkStatus;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
