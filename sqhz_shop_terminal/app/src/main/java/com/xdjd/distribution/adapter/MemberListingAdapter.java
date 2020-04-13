package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.MemberBean;
import com.xdjd.distribution.callback.ItemOnListener;
import com.xdjd.distribution.popup.MemberListingPopup;
import com.xdjd.steward.bean.SalesdocListBean;
import com.xdjd.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class MemberListingAdapter extends BaseAdapter {

    private List<MemberBean> list;
    private String memberId = null;

    private ItemOnListener listener;

    public void setId(String memberId) {
        this.memberId = memberId;
    }

    public MemberListingAdapter(ItemOnListener listener) {
        this.listener = listener;
    }

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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_member_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvName.setText(list.get(i).getMb_name());

        if (memberId != null && memberId.equals(list.get(i).getMb_id())) {
            holder.mIvCheck.setVisibility(View.VISIBLE);
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_blue));
        } else {
            holder.mIvCheck.setVisibility(View.GONE);
            holder.mTvName.setTextColor(UIUtils.getColor(R.color.text_gray));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    public void setData(List<MemberBean> list) {
        this.list = list;
        notifyDataSetInvalidated();
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.iv_check)
        ImageView mIvCheck;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
