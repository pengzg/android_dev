package com.xdjd.steward.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdjd.distribution.R;
import com.xdjd.steward.bean.VisitRateBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 *     author : lijipei
 *     time   : 2017/8/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class VisitRateAdapter extends BaseAdapter {

    public List<VisitRateBean> list;

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
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_visit_rate, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        VisitRateBean bean = list.get(i);

        holder.mTvIndex.setText((i+1)+"");
        holder.mTvName.setText(bean.getSalesid_nameref());
        holder.mTvVisitNum.setText("访店数:"+bean.getVisitNum());
        holder.mTvSignBillNum.setText("签单数:"+bean.getOrdernum());
        holder.mTvVisitRate.setText("达成率:"+bean.getRatio()+"%");

        return view;
    }

    public void setData(List<VisitRateBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_index)
        TextView mTvIndex;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_visit_num)
        TextView mTvVisitNum;
        @BindView(R.id.tv_sign_bill_num)
        TextView mTvSignBillNum;
        @BindView(R.id.tv_visit_rate)
        TextView mTvVisitRate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
