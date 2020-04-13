package com.xdjd.distribution.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.AddressListBean;
import com.xdjd.utils.LogUtils;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/5/4
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerAddressListAdapter extends BaseAdapter {

    public List<AddressListBean> list;

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
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_customer_address_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AddressListBean bean = list.get(i);

        /*String str = null;
        String currentLetter = bean.getPinyin().charAt(0) + "";
        // 根据上一个首字母,决定当前是否显示字母
        if (i == 0) {
            str = currentLetter;
        } else {
            // 上一个人的拼音的首字母
            String preLetter = list.get(i - 1).getPinyin().charAt(0) + "";
            if (!TextUtils.equals(preLetter, currentLetter)) {
                str = currentLetter;
            }
        }*/

        // 根据str是否为空,决定是否显示索引栏
//        holder.mTvIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
//        holder.mTvIndex.setText(currentLetter);
        if(!"".equals(list.get(i).getCc_image()) && null != list.get(i).getCc_image()){
            Glide.with(viewGroup.getContext()).
                    load(list.get(i).getCc_image()).
                    placeholder(R.mipmap.customer_img).
                    error(R.mipmap.customer_img).
                    into(holder.csImage);
        }
        holder.mTvName.setText(bean.getCc_name());
        holder.mTvAddress.setText(list.get(i).getCc_address());
        holder.mTvContactsName.setText(list.get(i).getCc_contacts_name());
        holder.mTvMobile.setText(list.get(i).getCc_contacts_mobile());

        if ("Y".equals(list.get(i).getCc_islocation())){
            holder.mIvLocation.setImageResource(R.mipmap.locate_been);
        }else if ("N".equals(list.get(i).getCc_islocation())){
            holder.mIvLocation.setImageResource(R.mipmap.locate_not);
        }

        return view;
    }

    public void setData(List<AddressListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_contacts_name)
        TextView mTvContactsName;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.tv_mobile)
        TextView mTvMobile;
        @BindView(R.id.iv_location)
        ImageView mIvLocation;
        @BindView(R.id.cs_image)
        CircleImageView csImage;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
