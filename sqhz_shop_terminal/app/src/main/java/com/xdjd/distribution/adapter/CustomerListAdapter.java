package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.CustomerBean;
import com.xdjd.utils.UIUtils;
import com.xdjd.view.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/4/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class CustomerListAdapter extends BaseAdapter {

    List<CustomerBean> list;

    public void setData(List<CustomerBean> list) {
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_customer_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if(!"".equals(list.get(i).getCc_image()) && null != list.get(i).getCc_image()){
            Glide.with(viewGroup.getContext()).load(list.get(i).getCc_image())
                    .error(R.mipmap.customer_img)
                    .placeholder(R.mipmap.customer_img)
                    .into(holder.csImage);
        }
        holder.mTvName.setText(list.get(i).getCc_name());
        holder.mTvContactsName.setText(list.get(i).getCc_contacts_name());
        holder.mTvMobile.setText(list.get(i).getCc_contacts_mobile());
        holder.mTvAddress.setText(list.get(i).getCc_address());

        if ("1".equals(list.get(i).getCc_checkstats())) {
            holder.mTvCheckstats.setText("未审核");
            holder.mTvCheckstats.setTextColor(UIUtils.getColor(R.color.text_red));
            //            holder.mTvCode.setVisibility(View.GONE);
        } else if ("2".equals(list.get(i).getCc_checkstats())) {
            holder.mTvCheckstats.setText("审核通过");
            holder.mTvCheckstats.setTextColor(UIUtils.getColor(R.color.text_blue));
            //            holder.mTvCode.setVisibility(View.VISIBLE);
        } else if ("3".equals(list.get(i).getCc_checkstats())) {
            holder.mTvCheckstats.setText("审核不通过");
            holder.mTvCheckstats.setTextColor(UIUtils.getColor(R.color.text_red));
            //            holder.mTvCode.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_mobile)
        TextView mTvMobile;
        @BindView(R.id.tv_address)
        TextView mTvAddress;
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_checkstats)
        TextView mTvCheckstats;
        @BindView(R.id.tv_contacts_name)
        TextView mTvContactsName;
        @BindView(R.id.cs_image)
        CircleImageView csImage;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
