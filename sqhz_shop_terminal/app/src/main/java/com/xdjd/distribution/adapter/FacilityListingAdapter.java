package com.xdjd.distribution.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.distribution.bean.EquipmentBean;
import com.xdjd.distribution.callback.ItemOnListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/11/9
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class FacilityListingAdapter extends BaseAdapter {

    List<EquipmentBean> list;
    private ItemOnListener listener;

    public void setData(List<EquipmentBean> list){
        this.list = list;
        notifyDataSetInvalidated();
    }

    public FacilityListingAdapter(ItemOnListener listener) {
        this.listener = listener;
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
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_facility_listing, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvName.setText(list.get(i).getMe_num());
        holder.mTvType.setText("设备类型:"+list.get(i).getMe_type_nameref());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItem(i);
            }
        });
        return view;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_type)
        TextView mTvType;
        @BindView(R.id.rl_del)
        RelativeLayout mRlDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
