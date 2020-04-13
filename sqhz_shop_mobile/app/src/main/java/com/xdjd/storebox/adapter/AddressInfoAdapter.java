package com.xdjd.storebox.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdjd.storebox.R;
import com.xdjd.storebox.bean.ReceiveAddressBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Freesyle_hong on 2016/12/1.
 */

public class AddressInfoAdapter extends BaseAdapter {
    public static int flag;
    private AddressListener listener;
    private List<ReceiveAddressBean> list;

    public AddressInfoAdapter(AddressListener listener) {
        this.listener = listener;
    }

    public void setData(List<ReceiveAddressBean> list) {
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

    @NonNull
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mainaddress_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.auto(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

       /* if(list.get(position).getUsa_is_default() == 1){
            viewHolder.defaultimage.setSelected(true);
        }else {
            viewHolder.defaultimage.setSelected(false);
        }*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.item(position);
            }
        });

        viewHolder.mEditLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.edit(position);
            }
        });
        /*viewHolder.setDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setDefault(position);
            }
        });*/

        /*删除,图标切换*/
        /*if (flag == 1) {
            Log.e("flag", "jiantou");
            viewHolder.image.setSelected(false);
            viewHolder.image.setEnabled(false);
            view.setEnabled(true);
        } else {
            //Log.e("id", Integer.toString(list.get(position).getUsa_isDefault()));
            if (list.get(position).getUsa_is_default() != 1) {
                viewHolder.image.setSelected(true);
                viewHolder.image.setEnabled(true);
                view.setEnabled(false);
            } else {
                viewHolder.image.setSelected(false);
                viewHolder.image.setEnabled(false);
                view.setEnabled(false);
            }
        }*/

        if (flag == 1) {
            viewHolder.mEditLl.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.mEditLl.setVisibility(View.VISIBLE);
        }

        viewHolder.addressName.setText(list.get(position).getUsa_receiver_name());
        viewHolder.addressPhone.setText(list.get(position).getUsa_mobile());
        viewHolder.address.setText(list.get(position).getUsa_address());
        return view;
    }

    public interface AddressListener {
        void item(int i);

        void edit(int i);
        //void setDefault(int i);
    }


    class ViewHolder {
        @BindView(R.id.address_name)
        TextView addressName;
        @BindView(R.id.address_phone)
        TextView addressPhone;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.image)/*删除图标*/
                ImageView image;
        @BindView(R.id.edit_ll)
        LinearLayout mEditLl;

        /* @BindView(R.id.set_default_address)*//*默认图标*//*
                LinearLayout  setDefaultAddress;*/
        /*@BindView(R.id.default_image)*//*//*
                ImageView defaultimage;*/
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}


