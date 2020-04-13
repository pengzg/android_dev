package com.xdjd.steward.adapter;

import android.text.Html;
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
 *     time   : 2017/9/4
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class StaffVisitListAdapter extends BaseAdapter {

    private List<VisitRateBean> list;

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
                    .inflate(R.layout.item_staff_visit_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        VisitRateBean bean = list.get(i);

        if (bean.getVisitNum()!=null && !"0".equals(bean.getVisitNum())){
            holder.mTvDate.setText(Html.fromHtml("<u>"+bean.getVisitDate()+"</u>"));
        }else{
            holder.mTvDate.setText(bean.getVisitDate());
        }


        holder.mTvVisitNum.setText(bean.getVisitNum());
        holder.mTvOrderNum.setText(bean.getOrdernum());
        holder.mTvRatio.setText(bean.getRatio()+"%");

        return view;
    }

    public void setData(List<VisitRateBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_visit_num)
        TextView mTvVisitNum;
        @BindView(R.id.tv_order_num)
        TextView mTvOrderNum;
        @BindView(R.id.tv_ratio)
        TextView mTvRatio;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
